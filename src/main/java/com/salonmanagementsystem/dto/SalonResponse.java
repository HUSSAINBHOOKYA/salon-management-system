package com.salonmanagementsystem.dto;

import com.salonmanagementsystem.enums.SalonStatus;

import lombok.Data;

@Data
public class SalonResponse {

    private Long id;

    private String salonName;

    private String ownerName;

    private String email;

    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String openingTime;

    private String closingTime;


    // =========================================
    // DESCRIPTION
    // =========================================

    private String description;


    // =========================================
    // RATING
    // =========================================

    private Double rating;

    private Integer reviewCount;


    // =========================================
    // LOGO
    // =========================================

    private String logoUrl;


    // =========================================
    // STATUS
    // =========================================

    private SalonStatus status;

}