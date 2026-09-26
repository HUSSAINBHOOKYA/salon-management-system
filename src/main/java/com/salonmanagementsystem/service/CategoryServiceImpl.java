package com.salonmanagementsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.CategoryRequest;
import com.salonmanagementsystem.dto.CategoryResponse;
import com.salonmanagementsystem.entity.Category;
import com.salonmanagementsystem.enums.CategoryStatus;
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

        this.categoryRepository =
                categoryRepository;

        this.fileUploadUtil =
                fileUploadUtil;
    }


    // =========================================
    // ADD CATEGORY
    // =========================================

    @Override
    public CategoryResponse addCategory(
            CategoryRequest request) {

        try {

            Category category =
                    new Category();


            // CATEGORY NAME

            category.setCategoryName(
                    request.getCategoryName()
            );


            // DESCRIPTION

            category.setDescription(
                    request.getDescription()
            );


            // STATUS

            category.setStatus(
                    request.getStatus() != null
                            ? request.getStatus()
                            : CategoryStatus.ACTIVE
            );


            // =====================================
            // IMAGE
            // =====================================
            // Image is optional.
            // Category can be created without image.

            if (
                    request.getImage() != null &&
                    !request.getImage().isEmpty()
            ) {

                String imageUrl =
                        fileUploadUtil.uploadFile(
                                request.getImage(),
                                "categories"
                        );

                category.setImageUrl(
                        imageUrl
                );

            }


            // SAVE

            Category savedCategory =
                    categoryRepository.save(
                            category
                    );


            return mapToResponse(
                    savedCategory
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "Image upload failed.",
                    e
            );

        }

    }


    // =========================================
    // GET ALL CATEGORIES
    // =========================================

    @Override
    public List<CategoryResponse>
    getAllCategories() {

        return categoryRepository
                .findAll()
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
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Category not found."
                                        )
                        );


        return mapToResponse(
                category
        );
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
                    categoryRepository
                            .findById(id)
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "Category not found."
                                            )
                            );


            // CATEGORY NAME

            if (
                    request.getCategoryName() != null &&
                    !request.getCategoryName().trim().isEmpty()
            ) {

                category.setCategoryName(
                        request.getCategoryName()
                );

            }


            // DESCRIPTION

            if (
                    request.getDescription() != null
            ) {

                category.setDescription(
                        request.getDescription()
                );

            }


            // STATUS

            if (
                    request.getStatus() != null
            ) {

                category.setStatus(
                        request.getStatus()
                );

            }


            // =====================================
            // UPDATE IMAGE ONLY WHEN PROVIDED
            // =====================================

            if (
                    request.getImage() != null &&
                    !request.getImage().isEmpty()
            ) {

                String imageUrl =
                        fileUploadUtil.uploadFile(
                                request.getImage(),
                                "categories"
                        );

                category.setImageUrl(
                        imageUrl
                );

            }


            // SAVE

            Category updatedCategory =
                    categoryRepository.save(
                            category
                    );


            return mapToResponse(
                    updatedCategory
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "Image upload failed.",
                    e
            );

        }

    }


    // =========================================
    // DELETE CATEGORY
    // =========================================

    @Override
    public void deleteCategory(
            Long id) {

        Category category =
                categoryRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Category not found."
                                        )
                        );


        categoryRepository.delete(
                category
        );
    }


    // =========================================
    // ENTITY → RESPONSE
    // =========================================

    private CategoryResponse mapToResponse(
            Category category) {

        CategoryResponse response =
                new CategoryResponse();


        response.setId(
                category.getId()
        );


        response.setCategoryName(
                category.getCategoryName()
        );


        response.setDescription(
                category.getDescription()
        );


        response.setImageUrl(
                category.getImageUrl()
        );


        response.setStatus(
                category.getStatus()
        );


        return response;
    }

}