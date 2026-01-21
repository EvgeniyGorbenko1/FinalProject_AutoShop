package com.tms.finalproject_autoshop.model.dto;

import lombok.Data;

@Data
public class PromoCreateDto {
    private String code;
    private Double discount;
    private Boolean isActive = true;
}
