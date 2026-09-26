package com.salonmanagementsystem.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    private Integer totalDuration;

    private Double totalPrice;


    // =========================================
    // BOOKING STATUS
    // =========================================

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;


    // =========================================
    // PAYMENT STATUS
    // =========================================

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
    // OLD APPOINTMENT → SERVICE
    // =========================================
    // Kept temporarily so existing appointments
    // remain compatible.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private SalonService salonService;


    // =========================================
    // APPOINTMENT → MULTIPLE SERVICES
    // =========================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "appointment_service_table",

            joinColumns = @JoinColumn(
                    name = "appointment_id"
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "service_id"
            )
    )
    @JsonIgnoreProperties({
            "employees",
            "category",
            "salon"
    })
    private List<SalonService> services =
            new ArrayList<>();


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