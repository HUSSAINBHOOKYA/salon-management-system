package com.salonmanagementsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.AIRequest;
import com.salonmanagementsystem.dto.AIResponse;
import com.salonmanagementsystem.service.AIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    // =========================================================
    // AI PRODUCT / SERVICE RECOMMENDATION
    // =========================================================

    @PostMapping("/recommend")
    public ResponseEntity<AIResponse> getRecommendation(
            @RequestBody AIRequest request) {

        AIResponse response =
                aiService.getRecommendation(request);

        return ResponseEntity.ok(response);
    }
}