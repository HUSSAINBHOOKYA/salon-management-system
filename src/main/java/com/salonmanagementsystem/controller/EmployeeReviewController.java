package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.EmployeeReviewRequest;
import com.salonmanagementsystem.entity.EmployeeReviewClass;
import com.salonmanagementsystem.service.EmployeeReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee-reviews")
@RequiredArgsConstructor
public class EmployeeReviewController {

    private final EmployeeReviewService employeeReviewService;

    @PostMapping
    public ResponseEntity<EmployeeReviewClass> addReview(
            @RequestBody EmployeeReviewRequest request,
            Authentication authentication) {

        EmployeeReviewClass review = employeeReviewService.addReview(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(review);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeReviewClass>> getEmployeeReviews(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                employeeReviewService.getEmployeeReviews(employeeId)
        );
    }
}