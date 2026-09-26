package com.salonmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.enums.PaymentStatus;

@Repository
public interface ProductOrderRepository
        extends JpaRepository<ProductOrderClass, Long> {

    // =========================================
    // SEARCH ORDERS BY CUSTOMER NAME
    // =========================================

    Page<ProductOrderClass> findByCustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );


    // =========================================
    // GET CUSTOMER'S ORDERS BY EMAIL
    // =========================================

    List<ProductOrderClass> findByCustomerEmailOrderByIdDesc(
            String customerEmail
    );


    // =========================================
    // PRODUCT ORDER EXISTS
    // =========================================

    boolean existsByProductId(Long productId);


    // =========================================
    // PAYMENT STATUS
    // =========================================

    long countByPaymentStatus(PaymentStatus paymentStatus);


    // =========================================
    // ORDER DATE
    // =========================================

    long countByOrderDate(LocalDate orderDate);


    // =========================================
    // TOTAL REVENUE
    // =========================================

    @Query("""
        SELECT COALESCE(SUM(p.totalPrice),0)
        FROM ProductOrderClass p
    """)
    Double getTotalRevenue();


    // =========================================
    // TODAY REVENUE
    // =========================================

    @Query("""
        SELECT COALESCE(SUM(p.totalPrice),0)
        FROM ProductOrderClass p
        WHERE p.orderDate = :date
    """)
    Double getTodayRevenue(LocalDate date);
    
    
    
    List<ProductOrderClass> findByProductId(Long productId);

}