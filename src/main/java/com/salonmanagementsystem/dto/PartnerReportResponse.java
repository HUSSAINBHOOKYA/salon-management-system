package com.salonmanagementsystem.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerReportResponse {

    // =========================================
    // SALON
    // =========================================

    private Long salonId;

    private String salonName;


    // =========================================
    // SUMMARY
    // =========================================

    private long totalEmployees;

    private long totalServices;

    private long totalProducts;

    private long totalAppointments;

    private long totalOrders;

    private long totalCustomers;


    // =========================================
    // APPOINTMENTS
    // =========================================

    private long bookedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private long todayAppointments;


    // =========================================
    // PRODUCTS
    // =========================================

    private long availableProducts;

    private long outOfStockProducts;

    private long discontinuedProducts;


    // =========================================
    // ORDERS
    // =========================================

    private long pendingOrders;

    private long paidOrders;

    private long failedOrders;


    // =========================================
    // REVENUE
    // =========================================

    private double appointmentRevenue;

    private double productRevenue;

    private double totalRevenue;

    private double todayRevenue;


    // =========================================
    // PERFORMANCE
    // =========================================

    private List<ServicePerformance> servicePerformance;

    private List<EmployeePerformance> employeePerformance;

    private List<ProductPerformance> productPerformance;


    // =========================================
    // SERVICE PERFORMANCE
    // =========================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicePerformance {

        private Long serviceId;

        private String serviceName;

        private long bookingCount;

        private double revenue;
    }


    // =========================================
    // EMPLOYEE PERFORMANCE
    // =========================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeePerformance {

        private Long employeeId;

        private String employeeName;

        private long appointmentCount;

        private double revenue;
    }


    // =========================================
    // PRODUCT PERFORMANCE
    // =========================================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPerformance {

        private Long productId;

        private String productName;

        private long quantitySold;

        private double revenue;

        private long orderCount;
    }
}