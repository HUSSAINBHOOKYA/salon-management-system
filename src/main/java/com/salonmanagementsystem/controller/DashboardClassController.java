package com.salonmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.entity.DashboardClass;
import com.salonmanagementsystem.service.DashboardClassService;

@RestController
public class DashboardClassController {

    @Autowired
    private DashboardClassService dashboardClassService;

    @GetMapping("/api/dashboard")
    public DashboardClass getDashboard() {

        return dashboardClassService.getDashboard();

    }

}