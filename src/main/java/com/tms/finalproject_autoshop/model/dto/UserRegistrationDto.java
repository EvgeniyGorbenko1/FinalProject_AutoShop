package com.tms.finalproject_autoshop.model.dto;

import com.tms.finalproject_autoshop.annotations.CustomAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @Pattern(regexp = "[A-z]{4,}")
    private String password;

    @NotBlank
    @Size(min = 2, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 20)
    private String lastName;

    @Email
    private String email;

    @CustomAge
    private Integer age;
}
