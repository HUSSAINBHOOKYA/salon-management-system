package com.salonmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.enums.PaymentStatus;

@Repository
public interface BillRepository
        extends JpaRepository<BillClass, Long> {

    // =========================================================
    // SEARCH BY CUSTOMER NAME
    // =========================================================

    Page<BillClass> findByCustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );

    // =========================================================
    // PAYMENT STATUS
    // =========================================================

    Page<BillClass> findByPaymentStatus(
            PaymentStatus paymentStatus,
            Pageable pageable
    );

    // =========================================================
    // CUSTOMER - MY BILLS
    // =========================================================

    List<BillClass> findByCustomerEmailIgnoreCase(
            String customerEmail
    );

    // =========================================================
    // FIND BILL BY PRODUCT ORDER
    // PRODUCT ORDER → BILL
    // =========================================================

    Optional<BillClass> findByProductOrderId(
            Long productOrderId
    );

    // =========================================================
    // FIND BILL BY APPOINTMENT
    // APPOINTMENT → BILL
    // =========================================================

    Optional<BillClass> findByAppointmentId(
            Long appointmentId
    );
    
    List<BillClass> findByAppointmentIdIn(List<Long> appointmentIds);

    List<BillClass> findByProductOrderIdIn(List<Long> productOrderIds);
}