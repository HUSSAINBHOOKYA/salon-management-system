package com.salonmanagementsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.CategoryRequest;
import com.salonmanagementsystem.dto.CategoryResponse;
import com.salonmanagementsystem.entity.Category;
import com.salonmanagementsystem.exception.ResourceNotFoundException;
import com.salonmanagementsystem.repository.CategoryRepository;
import com.salonmanagementsystem.util.FileUploadUtil;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final FileUploadUtil fileUploadUtil;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            FileUploadUtil fileUploadUtil) {

        this.categoryRepository = categoryRepository;
        this.fileUploadUtil = fileUploadUtil;
    }

    // =========================================
    // ADD CATEGORY
    // =========================================

    @Override
    public CategoryResponse addCategory(
            CategoryRequest request) {

        try {

            Category category = new Category();

            category.setCategoryName(
                    request.getCategoryName());

            category.setDescription(
                    request.getDescription());

            category.setStatus(
                    request.getStatus());

            // Image is required while creating
            if (request.getImage() == null ||
                    request.getImage().isEmpty()) {

                throw new RuntimeException(
                        "Category image is required.");
            }

            String imageUrl =
                    fileUploadUtil.uploadFile(
                            request.getImage(),
                            "categories");

            category.setImageUrl(imageUrl);

            Category savedCategory =
                    categoryRepository.save(category);

            return mapToResponse(savedCategory);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Image upload failed.", e);
        }
    }

    // =========================================
    // GET ALL CATEGORIES
    // =========================================

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================
    // GET CATEGORY BY ID
    // =========================================

    @Override
    public CategoryResponse getCategoryById(
            Long id) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found."));

        return mapToResponse(category);
    }

    // =========================================
    // UPDATE CATEGORY
    // =========================================

    @Override
    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        try {

            Category category =
                    categoryRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Category not found."));

            category.setCategoryName(
                    request.getCategoryName());

            category.setDescription(
                    request.getDescription());

            category.setStatus(
                    request.getStatus());

            // Update image only when new image exists
            if (request.getImage() != null &&
                    !request.getImage().isEmpty()) {

                String imageUrl =
                        fileUploadUtil.uploadFile(
                                request.getImage(),
                                "categories");

                category.setImageUrl(imageUrl);
            }

            Category updatedCategory =
                    categoryRepository.save(category);

            return mapToResponse(updatedCategory);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Image upload failed.", e);
        }
    }

    // =========================================
    // DELETE CATEGORY
    // =========================================

    @Override
    public void deleteCategory(Long id) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found."));

        categoryRepository.delete(category);
    }

    // =========================================
    // ENTITY → RESPONSE
    // =========================================

    private CategoryResponse mapToResponse(
            Category category) {

        CategoryResponse response =
                new CategoryResponse();

        response.setId(category.getId());

        response.setCategoryName(
                category.getCategoryName());

        response.setDescription(
                category.getDescription());

        response.setImageUrl(
                category.getImageUrl());

        response.setStatus(
                category.getStatus());

        return response;
    }
}