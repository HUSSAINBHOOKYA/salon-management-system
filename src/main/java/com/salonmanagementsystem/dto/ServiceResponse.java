package com.salonmanagementsystem.dto;

import com.salonmanagementsystem.enums.Gender;
import com.salonmanagementsystem.enums.ServiceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {

    private Long id;

    private String serviceName;

    private String description;

    private Double price;

    private Integer duration;

    private String imageUrl;


    // =========================================
    // GENDER
    // =========================================

    private Gender gender;


    // =========================================
    // CATEGORY
    // =========================================

    private Long categoryId;

    private String categoryName;


    // =========================================
    // SALON
    // =========================================

    private Long salonId;

    private String salonName;


    // =========================================
    // STATUS
    // =========================================

    private ServiceStatus status;

}