package com.salonmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerRegisterRequest {

    private String fullName;

    private String email;

    private String phoneNumber;

    private String password;
}