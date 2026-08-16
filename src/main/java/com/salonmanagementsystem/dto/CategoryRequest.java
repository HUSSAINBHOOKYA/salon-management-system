package com.salonmanagementsystem.dto;

import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.enums.CategoryStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category Name is required")
    private String categoryName;

    @NotBlank(message = "Description is required")
    private String description;

    private MultipartFile image;

    @NotNull(message = "Status is required")
    private CategoryStatus status;
}