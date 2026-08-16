package com.salonmanagementsystem.dto;

import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.enums.SalonStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SalonRequest {

    @NotBlank(message = "Salon Name is required")
    private String salonName;

    @NotBlank(message = "Owner Name is required")
    private String ownerName;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @NotBlank(message = "Opening Time is required")
    private String openingTime;

    @NotBlank(message = "Closing Time is required")
    private String closingTime;

    @NotBlank(message = "Description is required")
    private String description;


    // =========================================
    // RATING
    // =========================================

    private Double rating;

    private Integer reviewCount;


    // =========================================
    // LOGO
    // =========================================

    @NotNull(message = "Logo is required")
    private MultipartFile logo;


    // =========================================
    // STATUS
    // =========================================

    @NotNull(message = "Status is required")
    private SalonStatus status;

}