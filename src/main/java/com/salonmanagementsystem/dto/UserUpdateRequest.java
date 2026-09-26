package com.salonmanagementsystem.dto;

import com.salonmanagementsystem.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String fullName;

    private String phoneNumber;

    private Gender gender;

    private Integer age;
}