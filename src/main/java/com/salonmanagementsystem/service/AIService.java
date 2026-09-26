package com.salonmanagementsystem.service;

import com.salonmanagementsystem.dto.AIRequest;
import com.salonmanagementsystem.dto.AIResponse;

public interface AIService {

    AIResponse getRecommendation(AIRequest request);
}