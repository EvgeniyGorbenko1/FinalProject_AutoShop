package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

//@Data
@Scope("prototype")
@NoArgsConstructor
@Entity(name = "service_parts")
//@EqualsAndHashCode(exclude = {"catalog"})
//@ToString(exclude = {"catalog"})
public class ServiceParts extends ProductAbstract {
//    @Id
//    @SequenceGenerator(name = "service_parts_generator", sequenceName = "service_parts_id_seq", allocationSize = 1)
//    @GeneratedValue(generator = "service_parts_generator")
//    private Long id;
    @Getter
    @Setter
    private String carBrand;
//    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "catalog_id")
//    private Catalog catalog;


}
