package com.tms.finalproject_autoshop.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
@Data
public class AuthRequest {
    private String username;
    private String password;
}
