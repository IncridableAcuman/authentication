package com.app.backend.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {
    @Autowired
    public  JavaMailSender sender;

    public void sendMail(String to,String subject,String text){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        sender.send(message);
    }

    public void sendHTML(String to,String subject,String html) throws MessagingException {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html,true);
        sender.send(message);
    }
}
