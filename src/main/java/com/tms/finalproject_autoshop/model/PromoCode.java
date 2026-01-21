package com.tms.finalproject_autoshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "Promo")
@Data
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private Double discount;

    private Boolean isActive = true;
}
