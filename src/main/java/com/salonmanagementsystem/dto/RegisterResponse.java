package com.salonmanagementsystem.dto;

import lombok.Data;

@Data
public class RegisterResponse {

    private String message;

    private UserResponse user;

}