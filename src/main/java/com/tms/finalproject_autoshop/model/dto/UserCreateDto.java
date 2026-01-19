package com.tms.finalproject_autoshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserCreateDto {
    private String firstName;
    private String secondName;
    private String email;
    private int age;

    public UserCreateDto() {

    }
}
