package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@Tag(name = "Email", description = "Operations for sending system notifications")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(
            summary = "Send test notification email",
            description = "Sends a test email using the configured email service",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email sent successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied (ADMIN only)")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/notify")
    public ResponseEntity<String> notifyEmail() {
        emailService.sendEmail(
                "",
                "",
                ""
        );
        return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
    }
}
