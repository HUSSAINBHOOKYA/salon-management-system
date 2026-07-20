package com.salonmanagementsystem.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RegisterResponse {

	    private String message;
	    public UserEntity user;
}

