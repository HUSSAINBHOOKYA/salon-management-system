package com.salonmanagementsystem.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.enums.ProductStatus;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.ProductRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.util.FileUploadUtil;

@Service
public class ProductClassService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private FileUploadUtil fileUploadUtil;


    // =========================================
    // ADD PRODUCT
    // =========================================

    public ProductClass addProduct(
            ProductClass product,
            Long salonId,
            MultipartFile image)
            throws IOException {

        // =====================================
        // FIND SALON
        // =====================================

        Salon salon =
                salonRepository
                        .findById(salonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));


        // =====================================
        // UPLOAD IMAGE TO CLOUDINARY
        // =====================================

        if (image != null && !image.isEmpty()) {

            String imageUrl =
                    fileUploadUtil.uploadFile(
                            image,
                            "products");

            product.setImageUrl(imageUrl);
        }


        // =====================================
        // DEFAULT RATING
        // =====================================

        if (product.getRating() == null) {

            product.setRating(0.0);
        }


        // =====================================
        // DEFAULT REVIEW COUNT
        // =====================================

        if (product.getReviewCount() == null) {

            product.setReviewCount(0);
        }


        // =====================================
        // CONNECT PRODUCT → SALON
        // =====================================

        product.setSalon(salon);


        // =====================================
        // SAVE PRODUCT
        // =====================================

        return productRepository.save(product);
    }


    // =========================================
    // GET ALL PRODUCTS
    // =========================================

    public List<ProductClass> getAllProducts() {

        return productRepository.findAll();
    }


    // =========================================
    // GET PRODUCT BY ID
    // =========================================

    public ProductClass getProductById(Long id) {

        return productRepository
                .findById(id)
                .orElse(null);
    }


    // =========================================
    // UPDATE PRODUCT
    // =========================================

    public ProductClass updateProduct(
            Long id,
            ProductClass updatedProduct,
            Long salonId,
            MultipartFile image)
            throws IOException {

        // =====================================
        // FIND EXISTING PRODUCT
        // =====================================

        ProductClass existingProduct =
                productRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"));


        // =====================================
        // FIND SALON
        // =====================================

        Salon salon =
                salonRepository
                        .findById(salonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));


        // =====================================
        // UPDATE PRODUCT DETAILS
        // =====================================

        existingProduct.setProductName(
                updatedProduct.getProductName());

        existingProduct.setDescription(
                updatedProduct.getDescription());

        existingProduct.setCategory(
                updatedProduct.getCategory());

        existingProduct.setBrand(
                updatedProduct.getBrand());

        existingProduct.setPrice(
                updatedProduct.getPrice());

        existingProduct.setStockQuantity(
                updatedProduct.getStockQuantity());

        existingProduct.setStatus(
                updatedProduct.getStatus());


        // =====================================
        // UPDATE RATING
        // =====================================

        if (updatedProduct.getRating() != null) {

            existingProduct.setRating(
                    updatedProduct.getRating());
        }


        // =====================================
        // UPDATE REVIEW COUNT
        // =====================================

        if (updatedProduct.getReviewCount() != null) {

            existingProduct.setReviewCount(
                    updatedProduct.getReviewCount());
        }


        // =====================================
        // CONNECT PRODUCT → SALON
        // =====================================

        existingProduct.setSalon(salon);


        // =====================================
        // UPDATE IMAGE
        // =====================================
        //
        // No new image:
        //     Keep existing image URL.
        //
        // New image:
        //     Upload to Cloudinary.
        //     Save new Cloudinary URL.
        //
        // =====================================

        if (image != null && !image.isEmpty()) {

            String imageUrl =
                    fileUploadUtil.uploadFile(
                            image,
                            "products");

            existingProduct.setImageUrl(imageUrl);
        }


        // =====================================
        // SAVE UPDATED PRODUCT
        // =====================================

        return productRepository
                .save(existingProduct);
    }


    // =========================================
    // DELETE PRODUCT
    // =========================================

    public String deleteProduct(Long id) {

        ProductClass product =
                productRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product Not Found"));


        // =====================================
        // CHECK PRODUCT ORDERS
        // =====================================

        if (productOrderRepository
                .existsByProductId(id)) {

            product.setStatus(
                    ProductStatus.DISCONTINUED);

            productRepository.save(product);

            return "Product has existing orders, "
                    + "so it has been marked as "
                    + "DISCONTINUED.";
        }


        // =====================================
        // DELETE OLD LOCAL IMAGE ONLY
        // =====================================
        //
        // This is only for old products that
        // still contain:
        //
        // /uploads/products/...
        //
        // Cloudinary URLs are NOT treated
        // as local files.
        //
        // =====================================

        String imageUrl =
                product.getImageUrl();

        if (imageUrl != null
                && !imageUrl.isBlank()
                && imageUrl.startsWith("/uploads/")) {

            String imagePath =
                    imageUrl.replace(
                            "/uploads/",
                            "uploads/");

            File imageFile =
                    new File(imagePath);

            if (imageFile.exists()) {

                imageFile.delete();
            }
        }


        // =====================================
        // DELETE PRODUCT
        // =====================================

        productRepository.delete(product);

        return "Product Deleted Successfully";
    }


    // =========================================
    // SEARCH PRODUCT
    // =========================================

    public List<ProductClass> searchProduct(
            String name) {

        return productRepository
                .findByProductNameContainingIgnoreCase(
                        name);
    }


    // =========================================
    // PAGINATION
    // =========================================

    public PaginationResponse<ProductClass>
    getProductsWithPagination(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size);


        Page<ProductClass> productPage =
                productRepository
                        .findAll(pageable);


        return new PaginationResponse<>(
                productPage.getContent(),
                productPage.getNumber(),
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.getSize()
        );
    }


    // =========================================
    // GET PRODUCTS BY SALON
    // =========================================

    public List<ProductClass> getProductsBySalon(
            Long salonId) {

        return productRepository
                .findBySalonId(salonId);
    }

}





















//package com.salonmanagementsystem.service;
//
//import java.io.File;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.salonmanagementsystem.constants.AppConstants;
//import com.salonmanagementsystem.dto.PaginationResponse;
//import com.salonmanagementsystem.entity.ProductClass;
//import com.salonmanagementsystem.entity.Salon;
//import com.salonmanagementsystem.enums.ProductStatus;
//import com.salonmanagementsystem.repository.ProductOrderRepository;
//import com.salonmanagementsystem.repository.ProductRepository;
//import com.salonmanagementsystem.repository.SalonRepository;
//
//@Service
//public class ProductClassService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductOrderRepository productOrderRepository;
//
//    @Autowired
//    private SalonRepository salonRepository;
//
//
//    // =========================================
//    // ADD PRODUCT
//    // =========================================
//
//    public ProductClass addProduct(
//            ProductClass product,
//            Long salonId,
//            MultipartFile image)
//            throws IOException {
//
//        // =====================================
//        // FIND SALON
//        // =====================================
//
//        Salon salon =
//                salonRepository
//                        .findById(salonId)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Salon Not Found"));
//
//
//        // =====================================
//        // CREATE UPLOAD FOLDER
//        // =====================================
//
//        File uploadFolder =
//                new File(
//                        AppConstants.PRODUCT_UPLOAD_DIR);
//
//        if (!uploadFolder.exists()) {
//
//            uploadFolder.mkdirs();
//
//        }
//
//
//        // =====================================
//        // IMAGE FILE
//        // =====================================
//
//        String fileName =
//                UUID.randomUUID()
//                        + "_"
//                        + image.getOriginalFilename();
//
//
//        Path filePath =
//                Paths.get(
//                        AppConstants.PRODUCT_UPLOAD_DIR,
//                        fileName);
//
//
//        Files.copy(
//                image.getInputStream(),
//                filePath,
//                StandardCopyOption.REPLACE_EXISTING);
//
//
//        // =====================================
//        // IMAGE URL
//        // =====================================
//
//        product.setImageUrl(
//                "/uploads/products/"
//                        + fileName);
//
//
//        // =====================================
//        // DEFAULT RATING
//        // =====================================
//
//        if (product.getRating() == null) {
//
//            product.setRating(0.0);
//
//        }
//
//
//        // =====================================
//        // DEFAULT REVIEW COUNT
//        // =====================================
//
//        if (product.getReviewCount() == null) {
//
//            product.setReviewCount(0);
//
//        }
//
//
//        // =====================================
//        // CONNECT PRODUCT → SALON
//        // =====================================
//
//        product.setSalon(salon);
//
//
//        // =====================================
//        // SAVE
//        // =====================================
//
//        return productRepository.save(product);
//    }
//
//
//    // =========================================
//    // GET ALL PRODUCTS
//    // =========================================
//
//    public List<ProductClass>
//    getAllProducts() {
//
//        return productRepository.findAll();
//
//    }
//
//
//    // =========================================
//    // GET PRODUCT BY ID
//    // =========================================
//
//    public ProductClass
//    getProductById(Long id) {
//
//        return productRepository
//                .findById(id)
//                .orElse(null);
//
//    }
//
//
//    // =========================================
//    // UPDATE PRODUCT
//    // =========================================
//
//    public ProductClass updateProduct(
//
//            Long id,
//
//            ProductClass updatedProduct,
//
//            Long salonId,
//
//            MultipartFile image)
//
//            throws IOException {
//
//
//        // =====================================
//        // FIND EXISTING PRODUCT
//        // =====================================
//
//        ProductClass existingProduct =
//                productRepository
//                        .findById(id)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Product not found"));
//
//
//        // =====================================
//        // FIND SALON
//        // =====================================
//
//        Salon salon =
//                salonRepository
//                        .findById(salonId)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Salon Not Found"));
//
//
//        // =====================================
//        // UPDATE PRODUCT DETAILS
//        // =====================================
//
//        existingProduct.setProductName(
//                updatedProduct.getProductName());
//
//        existingProduct.setDescription(
//                updatedProduct.getDescription());
//
//        existingProduct.setCategory(
//                updatedProduct.getCategory());
//
//        existingProduct.setBrand(
//                updatedProduct.getBrand());
//
//        existingProduct.setPrice(
//                updatedProduct.getPrice());
//
//        existingProduct.setStockQuantity(
//                updatedProduct.getStockQuantity());
//
//        existingProduct.setStatus(
//                updatedProduct.getStatus());
//
//
//        // =====================================
//        // UPDATE RATING
//        // =====================================
//
//        if (updatedProduct.getRating() != null) {
//
//            existingProduct.setRating(
//                    updatedProduct.getRating());
//
//        }
//
//
//        // =====================================
//        // UPDATE REVIEW COUNT
//        // =====================================
//
//        if (updatedProduct.getReviewCount() != null) {
//
//            existingProduct.setReviewCount(
//                    updatedProduct.getReviewCount());
//
//        }
//
//
//        // =====================================
//        // CONNECT PRODUCT → SALON
//        // =====================================
//
//        existingProduct.setSalon(salon);
//
//
//        // =====================================
//        // UPDATE IMAGE
//        // =====================================
//
//        if (image != null &&
//                !image.isEmpty()) {
//
//
//            // Delete old image
//
//            if (existingProduct.getImageUrl() != null) {
//
//                File oldImage =
//                        new File(
//                                AppConstants.PRODUCT_UPLOAD_DIR
//                                        + existingProduct
//                                                .getImageUrl()
//                                                .replace(
//                                                        "/uploads/products/",
//                                                        ""));
//
//                if (oldImage.exists()) {
//
//                    oldImage.delete();
//
//                }
//
//            }
//
//
//            // Generate new filename
//
//            String fileName =
//                    UUID.randomUUID()
//                            + "_"
//                            + image.getOriginalFilename();
//
//
//            // Save new image
//
//            Path filePath =
//                    Paths.get(
//                            AppConstants.PRODUCT_UPLOAD_DIR,
//                            fileName);
//
//
//            Files.copy(
//                    image.getInputStream(),
//                    filePath,
//                    StandardCopyOption.REPLACE_EXISTING);
//
//
//            // Save new URL
//
//            existingProduct.setImageUrl(
//                    "/uploads/products/"
//                            + fileName);
//
//        }
//
//
//        // =====================================
//        // SAVE
//        // =====================================
//
//        return productRepository
//                .save(existingProduct);
//    }
//
//
//    // =========================================
//    // DELETE PRODUCT
//    // =========================================
//
//    public String deleteProduct(Long id) {
//
//        ProductClass product =
//                productRepository
//                        .findById(id)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Product Not Found"));
//
//
//        // =====================================
//        // CHECK PRODUCT ORDERS
//        // =====================================
//
//        if (productOrderRepository
//                .existsByProductId(id)) {
//
//            product.setStatus(
//                    ProductStatus.DISCONTINUED);
//
//            productRepository.save(product);
//
//            return "Product has existing orders, "
//                    + "so it has been marked as "
//                    + "DISCONTINUED.";
//        }
//
//
//        // =====================================
//        // DELETE IMAGE
//        // =====================================
//
//        if (product.getImageUrl() != null) {
//
//            File imageFile =
//                    new File(
//                            AppConstants.PRODUCT_UPLOAD_DIR
//                                    + product.getImageUrl()
//                                            .replace(
//                                                    "/uploads/products/",
//                                                    ""));
//
//            if (imageFile.exists()) {
//
//                imageFile.delete();
//
//            }
//
//        }
//
//
//        // =====================================
//        // DELETE PRODUCT
//        // =====================================
//
//        productRepository.delete(product);
//
//        return "Product Deleted Successfully";
//    }
//
//
//    // =========================================
//    // SEARCH PRODUCT
//    // =========================================
//
//    public List<ProductClass>
//    searchProduct(String name) {
//
//        return productRepository
//                .findByProductNameContainingIgnoreCase(
//                        name);
//
//    }
//
//
//    // =========================================
//    // PAGINATION
//    // =========================================
//
//    public PaginationResponse<ProductClass>
//    getProductsWithPagination(
//
//            int page,
//
//            int size) {
//
//
//        Pageable pageable =
//                PageRequest.of(
//                        page,
//                        size);
//
//
//        Page<ProductClass> productPage =
//                productRepository
//                        .findAll(pageable);
//
//
//        return new PaginationResponse<>(
//
//                productPage.getContent(),
//
//                productPage.getNumber(),
//
//                productPage.getTotalPages(),
//
//                productPage.getTotalElements(),
//
//                productPage.getSize()
//
//        );
//    }
//
//
//    // =========================================
//    // GET PRODUCTS BY SALON
//    // =========================================
//
//    public List<ProductClass>
//    getProductsBySalon(
//            Long salonId) {
//
//        return productRepository
//                .findBySalonId(salonId);
//
//    }
//
//}