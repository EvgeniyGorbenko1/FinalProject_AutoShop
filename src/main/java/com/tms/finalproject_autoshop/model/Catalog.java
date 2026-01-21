package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;


@Entity(name = "catalog")
@Data
public class Catalog {
    @Id
    @SequenceGenerator(name = "catalog_generator", sequenceName = "catalog_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "catalog_generator")
    private Long id;
    private String name;
    private String image;
    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "catalog", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<SpareParts> spareParts;
}
