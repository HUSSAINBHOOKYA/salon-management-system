package com.salonmanagementsystem.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salonmanagementsystem.enums.TimeSlotStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "timeSlotTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate slotDate;

    private String slotTime;

    @Enumerated(EnumType.STRING)
    private TimeSlotStatus slotStatus;

    // =========================================
    // TIME SLOT → SALON
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "salon_id",
            nullable = false
    )
    @JsonIgnore
    private Salon salon;

    // =========================================
    // TIME SLOT → SERVICE
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private SalonService salonService;

    // =========================================
    // TIME SLOT → EMPLOYEE
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private EmployeeClass employee;
}