package com.salonmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardClass {

    // Employee
    private Long totalEmployees;

    // Services
    private Long totalServices;

    // Products
    private Long totalProducts;
    private Long availableProducts;
    private Long outOfStockProducts;
    private Long discontinuedProducts;

    // Appointments
    private Long totalAppointments;
    private Long bookedAppointments;
    private Long todayAppointments;

    // Orders
    private Long totalOrders;
    private Long pendingOrders;
    private Long paidOrders;
    private Long failedOrders;
    private Long todayOrders;

    // Revenue
    private Double totalRevenue;
    private Double todayRevenue;

}