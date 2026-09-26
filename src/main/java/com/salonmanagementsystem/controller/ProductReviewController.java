package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.ProductReviewRequest;
import com.salonmanagementsystem.entity.ProductReviewClass;
import com.salonmanagementsystem.service.ProductReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product-reviews")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping
    public ResponseEntity<ProductReviewClass> addReview(
            @RequestBody ProductReviewRequest request,
            Authentication authentication) {

        ProductReviewClass review = productReviewService.addReview(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(review);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReviewClass>> getProductReviews(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                productReviewService.getProductReviews(productId)
        );
    }
}