package com.mrudula.order_management.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role; // "ADMIN" or "USER"
}
