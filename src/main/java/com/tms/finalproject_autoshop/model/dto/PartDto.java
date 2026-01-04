package com.tms.finalproject_autoshop.model.dto;

import com.tms.finalproject_autoshop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Data
public class PartDto {
    private String name;
    private String description;
    private BigDecimal price;
    private String image;
    private Category category;
    private Map<String, Object> specifications;
}
