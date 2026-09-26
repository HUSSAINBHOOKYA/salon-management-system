package com.salonmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {

    private Long productId;

    private Long productOrderId;

    private Integer rating;

    private String comment;
}