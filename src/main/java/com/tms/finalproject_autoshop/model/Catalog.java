package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Entity(name = "catalog")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"spare-parts","oil-parts"})
@ToString(exclude = {"spare-parts"})
public class Catalog {
    @Id
    @SequenceGenerator(name = "catalog_generator", sequenceName = "catalog_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "catalog_generator")
    private Long id;
    private String name;
    private String image;
    private String description;
//    @JsonBackReference
    @OneToMany(mappedBy = "catalog", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<SpareParts> spareParts;
    @OneToMany(mappedBy = "catalog", fetch = FetchType.EAGER)
    private Collection<ServiceParts> serviceParts;
    @OneToMany(mappedBy = "catalog", fetch = FetchType.EAGER)
    private Collection<OilParts> oilParts;


}
