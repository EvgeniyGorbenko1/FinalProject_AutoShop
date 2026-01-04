package com.tms.finalproject_autoshop.model.dto;

import com.tms.finalproject_autoshop.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartDto {
    private User user;
}
