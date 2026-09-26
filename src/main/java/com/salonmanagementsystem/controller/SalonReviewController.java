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

import com.salonmanagementsystem.dto.SalonReviewRequest;
import com.salonmanagementsystem.entity.SalonReviewClass;
import com.salonmanagementsystem.service.SalonReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salon-reviews")
@RequiredArgsConstructor
public class SalonReviewController {

    private final SalonReviewService salonReviewService;

    @PostMapping
    public ResponseEntity<SalonReviewClass> addReview(
            @RequestBody SalonReviewRequest request,
            Authentication authentication) {

        SalonReviewClass review = salonReviewService.addReview(
                request,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(review);
    }

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<SalonReviewClass>> getSalonReviews(
            @PathVariable Long salonId) {

        return ResponseEntity.ok(
                salonReviewService.getSalonReviews(salonId)
        );
    }
}