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
@Table(name = "appointmentTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String customerName;

    private String customerPhone;

    private String customerEmail;


    private LocalDate appointmentDate;

    private String appointmentTime;

    private String bookingStatus;

    private String paymentStatus;


    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceClass service;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeClass employee;


    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private TimeSlotClass timeSlot;

}