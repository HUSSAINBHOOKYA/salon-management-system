package com.salonmanagementsystem.dto;

import java.time.LocalDate;

import com.salonmanagementsystem.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBillResponse {

    // =========================================================
    // BILL
    // =========================================================

    private Long id;

    private String billNumber;


    // =========================================================
    // CUSTOMER
    // =========================================================

    private String customerName;

    private String customerPhone;

    private String customerEmail;


    // =========================================================
    // SALON
    // =========================================================

    private Long salonId;

    private String salonName;


    // =========================================================
    // APPOINTMENT
    // =========================================================

    private Long appointmentId;

    private LocalDate appointmentDate;

    private String appointmentTime;


    // =========================================================
    // SERVICE
    // =========================================================

    private String serviceName;


    // =========================================================
    // EMPLOYEE
    // =========================================================

    private String employeeName;


    // =========================================================
    // PRODUCT ORDER
    // =========================================================

    private Long productOrderId;

    private String productName;

    private String productImage;

    private Integer productQuantity;


    // =========================================================
    // AMOUNTS
    // =========================================================

    private Double serviceAmount;

    private Double productAmount;

    private Double totalAmount;


    // =========================================================
    // PAYMENT
    // =========================================================

    private String paymentMethod;

    private PaymentStatus paymentStatus;


    // =========================================================
    // BILL DATE
    // =========================================================

    private LocalDate billDate;
}