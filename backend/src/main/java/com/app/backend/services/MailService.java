package com.app.backend.services;

import com.app.backend.dto.ForgotPasswordEvent;
import com.app.backend.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailUtil mailUtil;
    @Value("${client.url}")
    private String clientUrl;

    @KafkaListener(topics = "forgot-password-topic",groupId = "email-group")
    public void sendResetEmail(ForgotPasswordEvent passwordEvent){
        String resetLink=clientUrl+"/reset-password?token="+passwordEvent.getToken();
        mailUtil.sendMail(passwordEvent.getEmail(), "Reset Password ", resetLink);
    }
}
