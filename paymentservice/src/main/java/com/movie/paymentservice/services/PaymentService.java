package com.movie.paymentservice.services;

import java.util.Base64;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.movie.paymentservice.configurations.MomoConfig;
import com.movie.paymentservice.dtos.requests.CreatePaymentReq;
import com.movie.paymentservice.dtos.requests.MomoCallback;
import com.movie.paymentservice.dtos.requests.MomoPaymentReq;
import com.movie.paymentservice.dtos.responses.MomoPaymentRes;
import com.movie.paymentservice.entities.Payment;
import com.movie.paymentservice.enums.PaymentMethod;
import com.movie.paymentservice.enums.PaymentStatus;
import com.movie.paymentservice.repositories.PaymentRepository;
import com.movie.paymentservice.repositories.httpCliens.BookingClient;
import com.movie.paymentservice.repositories.httpCliens.MomoClient;
import com.movie.paymentservice.utils.MomoSignatureUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MomoConfig momoConfig;
    private final MomoSignatureUtil momoSignatureUtil;
    private final PaymentRepository paymentRepository;
    private final BookingClient bookingClient;
    private final MomoClient momoClient;

    public String createPayment(CreatePaymentReq request) {
        String urlPayment = switch (request.getPaymentMethod()) {
            case MOMO -> createMomoPayment(request.getOrderId(), request.getAmount(), request.getOrderInfo());
            default -> null;
        };
        return urlPayment;
    }

    public boolean verifySignature(MomoCallback callback) {
        try {
            String rawData = String.format(
                    "accessKey=%s&amount=%d&extraData=%s&message=%s&orderId=%s&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s&responseTime=%d&resultCode=%d&transId=%d",
                    momoConfig.getAccessKey(),
                    callback.getAmount(),
                    callback.getExtraData(),
                    callback.getMessage(),
                    callback.getOrderId(),
                    callback.getOrderInfo(),
                    callback.getOrderType(),
                    callback.getPartnerCode(),
                    callback.getPayType(),
                    callback.getRequestId(),
                    callback.getResponseTime(),
                    callback.getResultCode(),
                    callback.getTransId());

            String generatedSignature = hmacSHA256(rawData, momoConfig.getAccessKey());

            return generatedSignature.equals(callback.getSignature());
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hash = hmac.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hash);
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

    public void processMomoCallback(MomoCallback payload) {
        if (!verifySignature(payload)) {
            throw new RuntimeException("Invalid MoMo signature");
        }

        String bookingId = payload.getOrderInfo();
        PaymentStatus status = payload.getResultCode() == 0 ? PaymentStatus.PAID : PaymentStatus.FAILED;

        bookingClient.updateStatusToPaidAndRedisSeatIds(Long.valueOf(bookingId), status);
        savePayment(payload, bookingId, status, PaymentMethod.MOMO);
    }

    private void savePayment(MomoCallback payload, String bookingId, PaymentStatus status,
            PaymentMethod paymentMethod) {
        Payment payment = Payment.builder()
                .orderId(payload.getOrderId())
                .bookingId(Long.valueOf(bookingId))
                .amount(payload.getAmount() / 1.0)
                .paymentStatus(status)
                .paymentMethod(paymentMethod)
                .transactionId(String.valueOf(payload.getTransId()))
                .requestId(payload.getRequestId())
                .message(payload.getMessage())
                .build();

        paymentRepository.save(payment);
    }
}
