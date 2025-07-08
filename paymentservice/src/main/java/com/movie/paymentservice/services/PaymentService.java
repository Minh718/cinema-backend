package com.movie.paymentservice.services;

import java.util.Base64;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.movie.paymentservice.configurations.MomoConfig;
import com.movie.paymentservice.dtos.requests.MomoPaymentRequest;
import com.movie.paymentservice.dtos.responses.MomoPaymentResponse;
import com.movie.paymentservice.utils.MomoSignatureUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final MomoConfig momoConfig;
    private final MomoSignatureUtil momoSignatureUtil;
    private final RestTemplate restTemplate;

    public MomoPaymentResponse createMomoPayment(String orderId, String amount, String orderInfo) {
        String requestId = UUID.randomUUID().toString();
        String extraData = Base64.getEncoder().encodeToString("booking".getBytes());

        MomoPaymentRequest req = MomoPaymentRequest.builder()
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MomoPaymentRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<MomoPaymentResponse> response = restTemplate.exchange(
                momoConfig.getEndpoint(), HttpMethod.POST, entity, MomoPaymentResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody(); // or deeplink
        }

        throw new RuntimeException("Failed to initiate MoMo payment");
    }
}
