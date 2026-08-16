package com.salonmanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.enums.ProductStatus;
import com.salonmanagementsystem.service.ProductClassService;

@RestController
@RequestMapping("/api/products")
public class ProductClassController {

    @Autowired
    private ProductClassService productClassService;


    // =========================================
    // ADD PRODUCT
    // =========================================

    @PostMapping(
            value = "/add",
            consumes = "multipart/form-data")
    public ProductClass addProduct(

            @RequestParam("productName")
            String productName,

            @RequestParam("description")
            String description,

            @RequestParam("category")
            String category,

            @RequestParam("brand")
            String brand,

            @RequestParam("price")
            Double price,

            @RequestParam("stockQuantity")
            Integer stockQuantity,

            @RequestParam("rating")
            Double rating,

            @RequestParam("reviewCount")
            Integer reviewCount,

            @RequestParam("status")
            ProductStatus status,

            @RequestParam("salonId")
            Long salonId,

            @RequestParam("image")
            MultipartFile image)

            throws IOException {


        ProductClass product =
                new ProductClass();


        product.setProductName(
                productName);

        product.setDescription(
                description);

        product.setCategory(
                category);

        product.setBrand(
                brand);

        product.setPrice(
                price);

        product.setStockQuantity(
                stockQuantity);

        product.setRating(
                rating);

        product.setReviewCount(
                reviewCount);

        product.setStatus(
                status);


        return productClassService
                .addProduct(
                        product,
                        salonId,
                        image);
    }


    // =========================================
    // GET ALL PRODUCTS
    // =========================================

    @GetMapping("/all")
    public List<ProductClass>
    getAllProducts() {

        return productClassService
                .getAllProducts();

    }


    // =========================================
    // GET PRODUCT BY ID
    // =========================================

    @GetMapping("/{id}")
    public ProductClass getProductById(
            @PathVariable Long id) {

        return productClassService
                .getProductById(id);

    }


    // =========================================
    // UPDATE PRODUCT
    // =========================================

    @PutMapping(
            value = "/update/{id}",
            consumes = "multipart/form-data")
    public ProductClass updateProduct(

            @PathVariable Long id,

            @RequestParam("productName")
            String productName,

            @RequestParam("description")
            String description,

            @RequestParam("category")
            String category,

            @RequestParam("brand")
            String brand,

            @RequestParam("price")
            Double price,

            @RequestParam("stockQuantity")
            Integer stockQuantity,

            @RequestParam("rating")
            Double rating,

            @RequestParam("reviewCount")
            Integer reviewCount,

            @RequestParam("status")
            ProductStatus status,

            @RequestParam("salonId")
            Long salonId,

            @RequestParam(
                    value = "image",
                    required = false)
            MultipartFile image)

            throws IOException {


        ProductClass product =
                new ProductClass();


        product.setProductName(
                productName);

        product.setDescription(
                description);

        product.setCategory(
                category);

        product.setBrand(
                brand);

        product.setPrice(
                price);

        product.setStockQuantity(
                stockQuantity);

        product.setRating(
                rating);

        product.setReviewCount(
                reviewCount);

        product.setStatus(
                status);


        return productClassService
                .updateProduct(
                        id,
                        product,
                        salonId,
                        image);
    }


    // =========================================
    // DELETE PRODUCT
    // =========================================

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        return productClassService
                .deleteProduct(id);

    }


    // =========================================
    // SEARCH PRODUCT
    // =========================================

    @GetMapping("/search")
    public List<ProductClass>
    searchProduct(
            @RequestParam String name) {

        return productClassService
                .searchProduct(name);

    }


    // =========================================
    // PAGINATION
    // =========================================

    @GetMapping("/pagination")
    public PaginationResponse<ProductClass>
    getProductsPagination(

            @RequestParam(
                    defaultValue = "0")
            int page,

            @RequestParam(
                    defaultValue = "10")
            int size) {

        return productClassService
                .getProductsWithPagination(
                        page,
                        size);

    }


    // =========================================
    // GET PRODUCTS BY SALON
    // =========================================

    @GetMapping("/salon/{salonId}")
    public List<ProductClass>
    getProductsBySalon(
            @PathVariable Long salonId) {

        return productClassService
                .getProductsBySalon(
                        salonId);

    }

}