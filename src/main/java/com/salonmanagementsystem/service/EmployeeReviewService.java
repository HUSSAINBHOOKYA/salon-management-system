package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.EmployeeReviewRequest;
import com.salonmanagementsystem.entity.EmployeeReviewClass;

public interface EmployeeReviewService {

    EmployeeReviewClass addReview(
            EmployeeReviewRequest request,
            String customerEmail
    );

    List<EmployeeReviewClass> getEmployeeReviews(Long employeeId);
}