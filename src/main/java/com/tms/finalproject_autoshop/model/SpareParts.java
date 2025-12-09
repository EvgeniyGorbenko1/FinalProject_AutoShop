package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Collection;

//@Data
@NoArgsConstructor
@Entity(name = "spare_parts")
//@EqualsAndHashCode(exclude = {"catalog"})
//@ToString(exclude = {"catalog"})
public class SpareParts extends ProductAbstract {
//    @Id
//    @SequenceGenerator(name = "original_parts_generator", sequenceName = "original_parts_id_seq", allocationSize = 1)
//    @GeneratedValue(generator = "original_parts_generator")
//    private Long id;
    @Getter
    @Setter
    private String carBrand;

    //    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "catalog_id")
//    private Catalog catalog;


}
