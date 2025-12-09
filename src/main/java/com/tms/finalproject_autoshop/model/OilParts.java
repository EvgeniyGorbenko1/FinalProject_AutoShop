package com.tms.finalproject_autoshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Entity(name = "oil_parts")

public class OilParts extends ProductAbstract {
    @Getter
    @Setter
    private String viscosity;



}
