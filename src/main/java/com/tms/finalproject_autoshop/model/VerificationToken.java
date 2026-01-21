package com.tms.finalproject_autoshop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name = "email_verification")

public class VerificationToken {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "security_id")
    private Security security;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

}
