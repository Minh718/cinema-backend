package com.movie.notificationservice.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.text.StringSubstitutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.movie.notificationservice.dtos.requests.EmailTemplateInfo;
import com.movie.notificationservice.enums.MailType;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    @Value("${spring.mail.username}")
    @NonFinal
    private String sender;

    public boolean sendEmail(String to, String content, EmailTemplateInfo emailTemplate) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, "utf-8");
            String htmlContent = buildEmailContent(emailTemplate.getMailType(), emailTemplate.getTemplateData());
            mailMessage.setFrom(sender);
            mailMessage.setTo(to);
            mailMessage.setSubject(emailTemplate.getMailType().getSubject());
            mailMessage.setText(htmlContent);

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // or log error
            return false;
        }
    }

    private String buildEmailContent(MailType type, Map<String, String> model) throws IOException {
        Resource cpr = resourceLoader.getResource("classpath:templates/email-template.html");
        String template = new String(FileCopyUtils.copyToByteArray(cpr.getInputStream()),
                StandardCharsets.UTF_8);
        StringSubstitutor sub = new StringSubstitutor(model);
        String emailContent = sub.replace(template);

        return emailContent;
    }
}
