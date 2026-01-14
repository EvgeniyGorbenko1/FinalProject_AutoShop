package com.tms.finalproject_autoshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateCatalogDto {
    private String name;
    private String description;
    private String image;

}
