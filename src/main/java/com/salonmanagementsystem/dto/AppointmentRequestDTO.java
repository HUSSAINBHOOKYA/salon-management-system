package com.salonmanagementsystem.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private Long salonId;

    private Long serviceId;

    private Long employeeId;

    private LocalDate appointmentDate;

    private String appointmentTime;

    private String bookingStatus;

    private String paymentStatus;
}