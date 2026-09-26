package com.salonmanagementsystem.dto;

import com.salonmanagementsystem.enums.Gender;
import com.salonmanagementsystem.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Gender gender;

    private Integer age;

    private Role role;
}