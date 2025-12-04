package com.tms.finalproject_autoshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PartDto {
    private String name;
    private String description;
    private double price;
    private String image;
    private String category;
}
