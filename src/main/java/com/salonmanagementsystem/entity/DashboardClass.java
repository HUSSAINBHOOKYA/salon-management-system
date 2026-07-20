package com.salonmanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardClass {

    private Long totalEmployees;

    private Long totalServices;

    private Long totalProducts;

    private Long availableProducts;

    private Long totalAppointments;

    private Long bookedAppointments;

    private Long totalOrders;

    private Double totalRevenue;

    private Long todayAppointments;

    private Double todayRevenue;

}