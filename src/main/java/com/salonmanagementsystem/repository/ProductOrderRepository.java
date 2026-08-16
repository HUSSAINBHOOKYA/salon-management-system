package com.salonmanagementsystem.repository;

import java.time.LocalDate;

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

    Page<ProductOrderClass> findByCustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );

    boolean existsByProductId(Long productId);

    long countByPaymentStatus(PaymentStatus paymentStatus);

    long countByOrderDate(LocalDate orderDate);

    @Query("""
        SELECT COALESCE(SUM(p.totalPrice),0)
        FROM ProductOrderClass p
    """)
    Double getTotalRevenue();

    @Query("""
        SELECT COALESCE(SUM(p.totalPrice),0)
        FROM ProductOrderClass p
        WHERE p.orderDate = :date
    """)
    Double getTodayRevenue(LocalDate date);

}