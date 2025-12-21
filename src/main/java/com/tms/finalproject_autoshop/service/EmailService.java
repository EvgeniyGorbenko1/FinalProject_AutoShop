 package com.tms.finalproject_autoshop.service;

 import jakarta.mail.MessagingException;
 import jakarta.mail.internet.MimeMessage;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.HttpStatusCode;
 import org.springframework.http.ResponseEntity;
 import org.springframework.mail.javamail.JavaMailSender;
 import org.springframework.mail.javamail.MimeMessageHelper;
 import org.springframework.stereotype.Service;

 @Slf4j
 @Service
 public class EmailService {

     private final JavaMailSender mailSender;


     public EmailService(JavaMailSender mailSender){
         this.mailSender = mailSender;
     }

     public ResponseEntity<HttpStatusCode> sendEmail(String to, String subject, String content){
         MimeMessage mimeMessage = mailSender.createMimeMessage();
         try {
             MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
             helper.setFrom("e37998274@gmail.com");
             helper.setTo(to);
             helper.setSubject(subject);
             helper.setText(content, true);
             mailSender.send(mimeMessage);
             log.info("Email sent successfully");
         } catch (MessagingException e){
             log.info("Email sent failed");
         }
         return ResponseEntity.status(HttpStatus.ACCEPTED).build();
     }
 }
