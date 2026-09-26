package com.salonmanagementsystem.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    // =========================================
    // CUSTOMER DETAILS
    // =========================================

    private String customerName;

    private String customerPhone;

    private String customerEmail;


    // =========================================
    // APPOINTMENT DETAILS
    // =========================================

    private LocalDate appointmentDate;

    private String appointmentTime;


    // =========================================
    // BOOKING STATUS
    // =========================================

    private String bookingStatus;


    // =========================================
    // PAYMENT STATUS
    // =========================================

    private String paymentStatus;


    // =========================================
    // SALON
    // =========================================

    private Long salonId;


    // =========================================
    // SERVICE
    // =========================================

    private List<Long> serviceIds;


    // =========================================
    // EMPLOYEE
    // =========================================

    private Long employeeId;


    // =========================================
    // TIME SLOT
    // =========================================

    private Long timeSlotId;
}