package com.salonmanagementsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private Double serviceAmount;

    private Double productAmount;

    private Double totalAmount;

    private String paymentMethod;

    private String paymentStatus;

    private LocalDate billDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentClass appointment;

    @ManyToOne
    @JoinColumn(name = "product_order_id")
    private ProductOrderClass productOrder;

}