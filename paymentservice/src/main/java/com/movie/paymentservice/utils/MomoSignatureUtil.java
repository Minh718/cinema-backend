package com.movie.paymentservice.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.movie.paymentservice.dtos.requests.MomoPaymentReq;

@Component
public class MomoSignatureUtil {
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String generateSignature(MomoPaymentReq req, String secretKey) {
        String raw = "accessKey=" + req.getAccessKey()
                + "&amount=" + req.getAmount()
                + "&extraData=" + req.getExtraData()
                + "&ipnUrl=" + req.getIpnUrl()
                + "&orderId=" + req.getOrderId()
                + "&orderInfo=" + req.getOrderInfo()
                + "&partnerCode=" + req.getPartnerCode()
                + "&redirectUrl=" + req.getRedirectUrl()
                + "&requestId=" + req.getRequestId()
                + "&requestType=" + req.getRequestType();

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(raw.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating MoMo signature", e);
        }
    }
}
