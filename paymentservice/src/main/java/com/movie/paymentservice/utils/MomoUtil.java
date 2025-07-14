package com.movie.paymentservice.utils;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.movie.paymentservice.dtos.requests.MomoCallback;

public class MomoUtil {
    public static String hmacSHA256(String data, String key) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmac.init(secretKeySpec);
        byte[] hash = hmac.doFinal(data.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifySignature(MomoCallback callback, String accessKey) {
        try {
            String rawData = String.format(
                    "accessKey=%s&amount=%d&extraData=%s&message=%s&orderId=%s&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s&responseTime=%d&resultCode=%d&transId=%d",
                    accessKey,
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

            String generatedSignature = hmacSHA256(rawData, accessKey);

            return generatedSignature.equals(callback.getSignature());
        } catch (Exception e) {
            return false;
        }
    }

}
