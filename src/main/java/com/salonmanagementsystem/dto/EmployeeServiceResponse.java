package com.salonmanagementsystem.dto;

import com.salonmanagementsystem.enums.EmployeeStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeServiceResponse {

    // =========================================
    // EMPLOYEE ID
    // =========================================

    private Long id;


    // =========================================
    // EMPLOYEE DETAILS
    // =========================================

    private String employeeName;

    private String employeeEmail;

    private String specialization;

    private String phoneNumber;

    private Integer experience;

    private Double salary;

    private String description;


    // =========================================
    // EMPLOYEE RATING
    // =========================================

    private Double rating;

    private Integer reviewCount;


    // =========================================
    // EMPLOYEE IMAGE
    // =========================================

    private String imageUrl;


    // =========================================
    // EMPLOYEE STATUS
    // =========================================

    private EmployeeStatus status;


    // =========================================
    // SALON
    // =========================================

    private Long salonId;

    private String salonName;

}