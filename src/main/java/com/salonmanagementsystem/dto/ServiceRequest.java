package com.salonmanagementsystem.dto;

import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.enums.Gender;
import com.salonmanagementsystem.enums.ServiceStatus;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceRequest {

    @NotBlank(message = "Service Name is required")
    private String serviceName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Min(
            value = 1,
            message = "Price must be greater than zero"
    )
    private Double price;

    @NotNull(message = "Duration is required")
    private Integer duration;

    // =========================================
    // SERVICE GENDER
    // =========================================

    @NotNull(message = "Gender is required")
    private Gender gender;

    // =========================================
    // CATEGORY
    // =========================================

    @NotNull(message = "Category is required")
    private Long categoryId;

    // =========================================
    // SALON
    // =========================================

    @NotNull(message = "Salon is required")
    private Long salonId;

    // =========================================
    // IMAGE
    // =========================================

    private MultipartFile image;

    // =========================================
    // STATUS
    // =========================================

    @NotNull(message = "Status is required")
    private ServiceStatus status;

}