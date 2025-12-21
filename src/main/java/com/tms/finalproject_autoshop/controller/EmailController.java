 package com.tms.finalproject_autoshop.controller;
 
 import com.tms.finalproject_autoshop.service.EmailService;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
 
 @RestController
 public class EmailController {
     private final EmailService emailService;
 
     public EmailController(EmailService emailService) {
         this.emailService = emailService;
     }
 
     @GetMapping("/notify")
     public ResponseEntity<String> notifyEmail() {
         emailService.sendEmail("",
                 "",
                 "");
         return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
     }
 
 }
