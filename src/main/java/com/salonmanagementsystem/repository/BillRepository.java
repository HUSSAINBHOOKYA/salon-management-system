package com.salonmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.enums.PaymentStatus;

@Repository
public interface BillRepository
        extends JpaRepository<BillClass, Long> {

    // Search Customer Name
    Page<BillClass> findByCustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );

    // Payment Status
    Page<BillClass> findByPaymentStatus(
            PaymentStatus paymentStatus,
            Pageable pageable
    );

}