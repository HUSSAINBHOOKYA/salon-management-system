package com.salonmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.enums.PaymentStatus;

@Repository
public interface AppointmentRepository
        extends JpaRepository<AppointmentClass, Long> {

    // ============================
    // Search Customer
    // ============================

    Page<AppointmentClass> findByCustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );


    // ============================
    // Customer's Appointments
    // ============================

    List<AppointmentClass> findByCustomerEmail(
            String customerEmail
    );


    // ============================
    // Date Filter
    // ============================

    List<AppointmentClass> findByAppointmentDate(
            LocalDate appointmentDate
    );


    // ============================
    // Employee Filter
    // ============================

    List<AppointmentClass> findByEmployeeId(
            Long employeeId
    );


    // ============================
    // Booking Status Filter
    // ============================

    Page<AppointmentClass> findByBookingStatus(
            BookingStatus bookingStatus,
            Pageable pageable
    );


    // ============================
    // Payment Status Filter
    // ============================

    Page<AppointmentClass> findByPaymentStatus(
            PaymentStatus paymentStatus,
            Pageable pageable
    );


    // ============================
    // Dashboard Counts
    // ============================

    long countByBookingStatus(
            BookingStatus bookingStatus
    );


    long countByAppointmentDate(
            LocalDate appointmentDate
    );
    
    List<AppointmentClass> findBySalonId(Long salonId);

}