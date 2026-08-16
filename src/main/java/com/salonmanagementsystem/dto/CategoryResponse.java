package com.salonmanagementsystem.dto;

import com.salonmanagementsystem.enums.CategoryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String categoryName;

    private String description;

    private String imageUrl;

    private CategoryStatus status;
}