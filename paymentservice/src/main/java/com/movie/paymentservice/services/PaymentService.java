package com.movie.paymentservice.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.paymentservice.configurations.MomoConfig;
import com.movie.paymentservice.configurations.VNPayConfig;
import com.movie.paymentservice.dtos.requests.BookingInfoReq;
import com.movie.paymentservice.dtos.requests.CreatePaymentReq;
import com.movie.paymentservice.dtos.requests.EmailTemplateInfo;
import com.movie.paymentservice.dtos.requests.MomoCallback;
import com.movie.paymentservice.dtos.requests.MomoPaymentReq;
import com.movie.paymentservice.dtos.responses.MomoPaymentRes;
import com.movie.paymentservice.entities.Payment;
import com.movie.paymentservice.enums.MailType;
import com.movie.paymentservice.enums.PaymentMethod;
import com.movie.paymentservice.enums.PaymentStatus;
import com.movie.paymentservice.events.models.NotificationEvent;
import com.movie.paymentservice.events.models.PaymentCompletedEvent;
import com.movie.paymentservice.events.publishers.NotificationKafkaPublisher;
import com.movie.paymentservice.events.publishers.PaymentEventPublisher;
import com.movie.paymentservice.repositories.PaymentRepository;
import com.movie.paymentservice.repositories.httpClients.BookingClient;
import com.movie.paymentservice.repositories.httpClients.MomoClient;
import com.movie.paymentservice.utils.MomoSignatureUtil;
import com.movie.paymentservice.utils.MomoUtil;
import com.movie.paymentservice.utils.VNPayUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MomoConfig momoConfig;
    private final VNPayConfig vnPayConfig;
    private final MomoSignatureUtil momoSignatureUtil;
    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;
    private final MomoClient momoClient;
    private final ObjectMapper ObjectMapper;
    private final NotificationKafkaPublisher notificationKafkaPublisher;
    private final PaymentEventPublisher paymentEventPublisher;

    public String createPayment(CreatePaymentReq request, HttpServletRequest httpServletRequest) {
        String urlPayment = switch (request.getPaymentMethod()) {
            case MOMO -> createMomoPayment(request.getOrderId(), request.getAmount(), request.getOrderInfo());
            case VNPAY ->
                createVnPayPayment(httpServletRequest, Double.valueOf(request.getAmount()), request.getOrderInfo());
            default -> null;
        };
        return urlPayment;
    }

    public String createMomoPayment(String orderId, String amount, String orderInfo) {
        String requestId = UUID.randomUUID().toString();
        String extraData = Base64.getEncoder().encodeToString("booking".getBytes());

        MomoPaymentReq req = MomoPaymentReq.builder()
                .partnerCode(momoConfig.getPartnerCode())
                .accessKey(momoConfig.getAccessKey())
                .requestId(requestId)
                .amount(amount)
                .orderId(orderId)
                .orderInfo(orderInfo)
                .redirectUrl(momoConfig.getRedirectUrl())
                .ipnUrl(momoConfig.getIpnUrl())
                .extraData(extraData)
                .requestType("captureWallet")
                .build();

        req.setSignature(momoSignatureUtil.generateSignature(req, momoConfig.getSecretKey()));

        MomoPaymentRes response = momoClient.createMomoPayment(req);
        if (response != null) {
            return response.getPayUrl(); // or deeplink
        }

        throw new RuntimeException("Failed to initiate MoMo payment");
    }

    public void processMomoCallback(MomoCallback payload) throws JsonMappingException, JsonProcessingException {
        if (!MomoUtil.verifySignature(payload, momoConfig.getAccessKey())) {
            throw new RuntimeException("Invalid MoMo signature");
        }

        String json = payload.getOrderInfo();
        BookingInfoReq bookingInfoReq = ObjectMapper.readValue(json, BookingInfoReq.class);
        PaymentStatus status = payload.getResultCode() == 0 ? PaymentStatus.PAID : PaymentStatus.FAILED;

        processBookingAndPayment(
                bookingInfoReq,
                payload.getTransId(),
                payload.getAmount(),
                status,
                PaymentMethod.MOMO,
                payload.getMessage());
    }

    public void processVnpayCallback(Map<String, String> reqParams)
            throws JsonMappingException, JsonProcessingException {
        String vnp_SecureHash = reqParams.remove("vnp_SecureHash");
        if (!vnp_SecureHash.equals(VNPayUtil.getVnpSecureHash(reqParams, vnPayConfig.getSecretKey()))) {
            throw new RuntimeException("Invalid VNPAY signature");
        }
        String json = reqParams.get("vnp_OrderInfo");
        BookingInfoReq bookingInfoReq = ObjectMapper.readValue(json, BookingInfoReq.class);

        String transId = reqParams.get("vnp_TransactionNo");
        long amount = Long.parseLong(reqParams.get("vnp_Amount")) / 2500000;

        PaymentStatus status = reqParams.get("vnp_ResponseCode").equals("00") ? PaymentStatus.PAID
                : PaymentStatus.FAILED;
        processBookingAndPayment(
                bookingInfoReq,
                transId,
                amount,
                status,
                PaymentMethod.VNPAY,
                vnp_SecureHash);
    }

    private void processBookingAndPayment(BookingInfoReq bookingInfo, String transId, long amount,
            PaymentStatus status, PaymentMethod method, String message) {
        paymentEventPublisher
                .publishPaymentCompletedEvent(new PaymentCompletedEvent(bookingInfo.getBookingId(), status));

        Payment payment = Payment.builder()
                .bookingId(bookingInfo.getBookingId())
                .amount(amount / 1.0)
                .paymentStatus(status)
                .paymentMethod(method)
                .transactionId(transId)
                .message(message)
                .build();
        paymentRepository.save(payment);

        EmailTemplateInfo emailTemplate = new EmailTemplateInfo(MailType.BOOKING_CONFIRMATION, bookingInfo.toMap());
        NotificationEvent event = NotificationEvent.builder()
                .to(bookingInfo.getEmail())
                .emailTemplate(emailTemplate)
                .build();
        if (status == PaymentStatus.PAID)
            notificationKafkaPublisher.pubishNotificationEvent(event);
    }

    public String createVnPayPayment(HttpServletRequest request, double amount, String orderInfo) {
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();

        double exchangeRate = 25000d;
        long vndAmount = Math.round(amount * exchangeRate); // USD -> VND
        long vnpAmount = vndAmount * 100; // Multiply by 100 as VNPAY expects smallest unit

        vnpParamsMap.put("vnp_Amount", String.valueOf(vnpAmount));
        vnpParamsMap.put("vnp_OrderInfo", orderInfo);
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        List<String> fieldNames = new ArrayList<>(vnpParamsMap.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder queryUrl = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnpParamsMap.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII);
                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII);
                hashData.append(encodedName).append('=').append(encodedValue).append('&');
                queryUrl.append(encodedName).append('=').append(encodedValue).append('&');
            }
        }

        if (hashData.length() > 0)
            hashData.setLength(hashData.length() - 1);
        if (queryUrl.length() > 0)
            queryUrl.setLength(queryUrl.length() - 1);
        String secureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData.toString());

        queryUrl.append("&vnp_SecureHash=").append(secureHash);

        return vnPayConfig.getVnpPayUrl() + "?" + queryUrl.toString();
    }
}
