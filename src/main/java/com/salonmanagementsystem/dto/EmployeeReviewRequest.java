package com.salonmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReviewRequest {

    private Long employeeId;

    private Long appointmentId;

    private Integer rating;

    private String comment;
}