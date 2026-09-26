package com.salonmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.DashboardResponse;
import com.salonmanagementsystem.dto.PartnerReportResponse;
import com.salonmanagementsystem.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;


    // =========================================
    // ADMIN / GLOBAL DASHBOARD
    // =========================================

    @GetMapping
    public DashboardResponse getDashboard() {

        return dashboardService.getDashboard();
    }


    // =========================================
    // PARTNER REPORTS
    // =========================================

    @GetMapping("/partner")
    public PartnerReportResponse getPartnerReports(
            Authentication authentication) {

        return dashboardService.getPartnerReports(
                authentication.getName()
        );
    }

}