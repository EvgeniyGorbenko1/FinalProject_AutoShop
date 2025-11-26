package com.tms.finalproject_autoshop.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Timestamp created;
    private Timestamp updated;
}
