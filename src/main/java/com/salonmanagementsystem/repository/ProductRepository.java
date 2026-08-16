package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.enums.ProductStatus;

@Repository
public interface ProductRepository
        extends JpaRepository<ProductClass, Long> {

    // =========================================
    // SEARCH
    // =========================================

    List<ProductClass>
    findByProductNameContainingIgnoreCase(
            String productName);


    // =========================================
    // PAGINATION
    // =========================================

    Page<ProductClass>
    findAll(Pageable pageable);


    // =========================================
    // STATUS COUNT
    // =========================================

    long countByStatus(
            ProductStatus status);


    // =========================================
    // SALON FILTER
    // =========================================

    List<ProductClass>
    findBySalonId(Long salonId);

}