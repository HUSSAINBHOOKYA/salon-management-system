package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.ProductReviewClass;

@Repository
public interface ProductReviewRepository
        extends JpaRepository<ProductReviewClass, Long> {

    List<ProductReviewClass> findByProductIdOrderByCreatedAtDesc(Long productId);

    boolean existsByProductOrderId(Long productOrderId);

    long countByProductId(Long productId);
}