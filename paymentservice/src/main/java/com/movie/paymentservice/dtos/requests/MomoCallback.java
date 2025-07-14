package com.movie.paymentservice.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomoCallback {

    private String partnerCode;
    private String orderId;
    private String requestId;
    private Long amount;
    private String orderInfo;
    private String orderType;

    private Long transId;
    private Integer resultCode; // 0 = success, others = failure
    private String message;

    private String payType;
    private Long responseTime;
    private String extraData;

    private String signature; // HMAC SHA256 to verify authenticity
}
