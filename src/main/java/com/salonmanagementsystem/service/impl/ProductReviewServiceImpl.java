package com.salonmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.ProductReviewRequest;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.entity.ProductReviewClass;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.ProductRepository;
import com.salonmanagementsystem.repository.ProductReviewRepository;
import com.salonmanagementsystem.service.ProductReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

    private final ProductReviewRepository productReviewRepository;

    private final ProductOrderRepository productOrderRepository;

    private final ProductRepository productRepository;


    @Override
    @Transactional
    public ProductReviewClass addReview(
            ProductReviewRequest request,
            String customerEmail) {

        // =========================================
        // VALIDATE PRODUCT ID
        // =========================================

        if (request.getProductId() == null) {

            throw new RuntimeException(
                    "Product ID is required"
            );

        }


        // =========================================
        // VALIDATE PRODUCT ORDER ID
        // =========================================

        if (request.getProductOrderId() == null) {

            throw new RuntimeException(
                    "Product order ID is required"
            );

        }


        // =========================================
        // VALIDATE RATING
        // =========================================

        validateRating(
                request.getRating()
        );


        // =========================================
        // GET PRODUCT
        // =========================================

        ProductClass product =
                productRepository
                        .findById(request.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );


        // =========================================
        // GET PRODUCT ORDER
        // =========================================

        ProductOrderClass order =
                productOrderRepository
                        .findById(
                                request.getProductOrderId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product order not found"
                                )
                        );


        // =========================================
        // VERIFY CUSTOMER
        // =========================================

        if (
                order.getCustomerEmail() == null
                        ||
                !order.getCustomerEmail()
                        .equalsIgnoreCase(customerEmail)
        ) {

            throw new RuntimeException(
                    "You can review only products purchased from your account"
            );

        }


        // =========================================
        // VERIFY PAYMENT
        // =========================================

        if (
                order.getPaymentStatus()
                        != PaymentStatus.PAID
        ) {

            throw new RuntimeException(
                    "You can review a product only after successful payment"
            );

        }


        // =========================================
        // VERIFY PRODUCT
        // =========================================

        if (
                order.getProduct() == null
                        ||
                order.getProduct().getId() == null
                        ||
                !order.getProduct()
                        .getId()
                        .equals(product.getId())
        ) {

            throw new RuntimeException(
                    "This product was not purchased in the selected order"
            );

        }


        // =========================================
        // PREVENT DUPLICATE REVIEW
        // =========================================

        if (
                productReviewRepository
                        .existsByProductOrderId(
                                order.getId()
                        )
        ) {

            throw new RuntimeException(
                    "You have already reviewed this purchase"
            );

        }


        // =========================================
        // CREATE REVIEW
        // =========================================

        ProductReviewClass review =
                new ProductReviewClass();


        review.setProduct(product);

        review.setProductOrder(order);

        review.setCustomerName(
                order.getCustomerName()
        );

        review.setCustomerEmail(
                customerEmail
        );

        review.setRating(
                request.getRating()
        );

        review.setComment(
                request.getComment()
        );

        review.setCreatedAt(
                LocalDateTime.now()
        );


        // =========================================
        // SAVE REVIEW
        // =========================================

        ProductReviewClass savedReview =
                productReviewRepository.save(
                        review
                );


        // =========================================
        // UPDATE PRODUCT RATING
        // =========================================

        updateProductRating(product);


        return savedReview;

    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductReviewClass> getProductReviews(
            Long productId) {

        // =========================================
        // VERIFY PRODUCT
        // =========================================

        if (
                !productRepository.existsById(
                        productId
                )
        ) {

            throw new RuntimeException(
                    "Product not found"
            );

        }


        // =========================================
        // GET REVIEWS
        // =========================================

        return productReviewRepository
                .findByProductIdOrderByCreatedAtDesc(
                        productId
                );

    }


    // =============================================
    // UPDATE PRODUCT RATING
    // =============================================

    private void updateProductRating(
            ProductClass product) {

        List<ProductReviewClass> reviews =
                productReviewRepository
                        .findByProductIdOrderByCreatedAtDesc(
                                product.getId()
                        );


        double averageRating =
                reviews.stream()

                        .mapToInt(
                                ProductReviewClass::getRating
                        )

                        .average()

                        .orElse(0.0);


        product.setRating(

                Math.round(
                        averageRating * 100.0
                ) / 100.0

        );


        product.setReviewCount(
                reviews.size()
        );


        productRepository.save(
                product
        );

    }


    // =============================================
    // VALIDATE RATING
    // =============================================

    private void validateRating(
            Integer rating) {

        if (
                rating == null
                        ||
                rating < 1
                        ||
                rating > 5
        ) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );

        }

    }

}