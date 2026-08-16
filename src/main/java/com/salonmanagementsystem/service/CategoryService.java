package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.CategoryRequest;
import com.salonmanagementsystem.dto.CategoryResponse;

public interface CategoryService {

    CategoryResponse addCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);

}