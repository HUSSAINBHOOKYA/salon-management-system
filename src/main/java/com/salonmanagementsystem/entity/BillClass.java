package com.salonmanagementsystem.entity;

import java.time.LocalDate;

import com.salonmanagementsystem.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "billTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillClass {

    // =========================================================
    // BILL ID
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================================
    // BILL NUMBER
    // =========================================================

    private String billNumber;


    // =========================================================
    // CUSTOMER DETAILS
    // =========================================================

    private String customerName;

    private String customerPhone;

    private String customerEmail;


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

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    // =========================================================
    // BILL DATE
    // =========================================================

    private LocalDate billDate;


    // =========================================================
    // APPOINTMENT
    // =========================================================

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentClass appointment;


    // =========================================================
    // PRODUCT ORDER
    // =========================================================

    @ManyToOne
    @JoinColumn(name = "product_order_id")
    private ProductOrderClass productOrder;
}