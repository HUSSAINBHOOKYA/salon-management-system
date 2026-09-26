package com.salonmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalonReviewRequest {

    private Long salonId;

    private Long appointmentId;

    private Integer rating;

    private String comment;
}