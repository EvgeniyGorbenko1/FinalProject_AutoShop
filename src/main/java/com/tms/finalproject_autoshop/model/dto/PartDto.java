package com.tms.finalproject_autoshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PartDto {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
    private Integer article;
    private String carBrand;
    private String catalog;
}
