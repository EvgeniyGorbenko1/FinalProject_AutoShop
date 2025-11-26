package com.tms.finalproject_autoshop.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class SpareParts {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;
    private String category;
}
