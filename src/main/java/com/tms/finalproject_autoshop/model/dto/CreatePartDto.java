package com.tms.finalproject_autoshop.model.dto;

import com.tms.finalproject_autoshop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class CreatePartDto {
    private String name;
    private String description;
    private Double price;
    private String image;
    private Category category;
    private Map<String, Object> specifications;
}
