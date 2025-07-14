package com.movie.paymentservice.dtos.requests;

import java.util.Map;

import com.movie.paymentservice.enums.MailType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplateInfo {
    private MailType mailType;
    private Map<String, String> templateData;
}
