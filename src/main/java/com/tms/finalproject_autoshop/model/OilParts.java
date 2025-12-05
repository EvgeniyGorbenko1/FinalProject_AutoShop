package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity(name = "oil_parts")
@EqualsAndHashCode(exclude = {"catalog"})
@ToString(exclude = {"catalog"})
public class OilParts {
    @Id
    @SequenceGenerator(name = "oil_parts_generator", sequenceName = "oil_parts_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "oil_parts_generator")
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private String image;
    private Integer article;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;


}
