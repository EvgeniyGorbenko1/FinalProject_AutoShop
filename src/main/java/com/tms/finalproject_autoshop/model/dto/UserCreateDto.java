package com.tms.finalproject_autoshop.model.dto;

import lombok.Data;

@Data
public class UserCreateDto {
    private String firstName;
    private String secondName;
    private String email;
    private int age;
}
