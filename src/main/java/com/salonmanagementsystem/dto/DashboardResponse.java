package com.salonmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalCategories;

    private Long totalEmployees;

    private Long totalServices;

    private Long totalProducts;
    private Long availableProducts;
    private Long outOfStockProducts;
    private Long discontinuedProducts;

    private Long totalAppointments;
    private Long bookedAppointments;
    private Long todayAppointments;

    private Long totalOrders;
    private Long pendingOrders;
    private Long paidOrders;
    private Long failedOrders;
    private Long todayOrders;

    private Double totalRevenue;
    private Double todayRevenue;

}