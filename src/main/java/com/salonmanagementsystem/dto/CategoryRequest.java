package com.salonmanagementsystem.dto;

import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.enums.CategoryStatus;

import lombok.Data;

@Data
public class CategoryRequest {

    private String categoryName;

    private String description;

    private MultipartFile image;

    private CategoryStatus status;
}