package com.tms.finalproject_autoshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Logger logger;

    public EmailService(JavaMailSender mailSender, Logger logger){
        this.mailSender = mailSender;
        this.logger = logger;
    }

    public void sendEmail(String to, String subject, String content){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
            logger.info("Email sent successfully");
        } catch (MessagingException e){
            logger.info("Email sent failed");
        }

    }
}
