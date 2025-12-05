package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Data
@NoArgsConstructor
@Entity(name = "spare_parts")
@EqualsAndHashCode(exclude = {"catalog"})
@ToString(exclude = {"catalog"})
public class SpareParts {
    @Id
    @SequenceGenerator(name = "spare_parts_generator", sequenceName = "spare_parts_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "spare_parts_generator")
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;
    private String category;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;


}
