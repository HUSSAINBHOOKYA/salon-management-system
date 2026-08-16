package com.salonmanagementsystem.entity;

import java.time.LocalDate;

import com.salonmanagementsystem.enums.BookingStatus;
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
@Table(name = "appointmentTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // =========================================
    // APPOINTMENT → SALON
    // =========================================

    @ManyToOne
    @JoinColumn(
            name = "salon_id",
            nullable = false
    )
    private Salon salon;

    // =========================================
    // APPOINTMENT → SERVICE
    // =========================================

    @ManyToOne
    @JoinColumn(name = "service_id")
    private SalonService salonService;

    // =========================================
    // APPOINTMENT → EMPLOYEE
    // =========================================

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeClass employee;

    // =========================================
    // APPOINTMENT → TIME SLOT
    // =========================================

    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private TimeSlotClass timeSlot;
}