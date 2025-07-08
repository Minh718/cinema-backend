package com.movie.paymentservice.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "momo")
@Data
public class MomoConfig {
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String redirectUrl;
    private String ipnUrl;
    private String endpoint;
}
