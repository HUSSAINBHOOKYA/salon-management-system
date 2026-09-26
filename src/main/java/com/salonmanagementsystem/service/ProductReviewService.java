package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.ProductReviewRequest;
import com.salonmanagementsystem.entity.ProductReviewClass;

public interface ProductReviewService {

    ProductReviewClass addReview(
            ProductReviewRequest request,
            String customerEmail
    );

    List<ProductReviewClass> getProductReviews(Long productId);
}