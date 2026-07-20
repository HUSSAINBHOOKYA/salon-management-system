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

    private String slotStatus;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceClass service;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeClass employee;

}