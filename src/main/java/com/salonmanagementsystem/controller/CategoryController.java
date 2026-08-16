package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.ApiResponse;
import com.salonmanagementsystem.dto.CategoryRequest;
import com.salonmanagementsystem.dto.CategoryResponse;
import com.salonmanagementsystem.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    // =========================================
    // ADD CATEGORY
    // =========================================

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CategoryResponse>>
    addCategory(
            @Valid @ModelAttribute CategoryRequest request) {

        CategoryResponse response =
                categoryService.addCategory(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Category Created Successfully",
                        response
                )
        );
    }

    // =========================================
    // GET ALL CATEGORIES
    // =========================================

    @GetMapping
    public ResponseEntity<List<CategoryResponse>>
    getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }

    // =========================================
    // GET CATEGORY BY ID
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse>
    getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(id)
        );
    }

    // =========================================
    // UPDATE CATEGORY
    // =========================================

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryResponse>
    updateCategory(
            @PathVariable Long id,

            @Valid @ModelAttribute
            CategoryRequest request) {

        return ResponseEntity.ok(
                categoryService.updateCategory(
                        id,
                        request)
        );
    }

    // =========================================
    // DELETE CATEGORY
    // =========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteCategory(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                "Category deleted successfully."
        );
    }
}