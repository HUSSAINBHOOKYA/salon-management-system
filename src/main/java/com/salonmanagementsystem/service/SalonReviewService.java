package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.SalonReviewRequest;
import com.salonmanagementsystem.entity.SalonReviewClass;

public interface SalonReviewService {

    SalonReviewClass addReview(
            SalonReviewRequest request,
            String customerEmail
    );

    List<SalonReviewClass> getSalonReviews(Long salonId);
}