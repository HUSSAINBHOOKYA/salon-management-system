package com.salonmanagementsystem.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.DashboardResponse;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.enums.ProductStatus;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.ProductRepository;
import com.salonmanagementsystem.repository.ServiceRepository;

@Service
public class DashboardService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    public DashboardResponse getDashboard() {

        DashboardResponse dashboard = new DashboardResponse();

        // Employee
        dashboard.setTotalEmployees(
                employeeRepository.count());

        // Services
        dashboard.setTotalServices(
                serviceRepository.count());

        // Products
        dashboard.setTotalProducts(
                productRepository.count());

        dashboard.setAvailableProducts(
                productRepository.countByStatus(ProductStatus.AVAILABLE));

        dashboard.setOutOfStockProducts(
                productRepository.countByStatus(ProductStatus.OUT_OF_STOCK));

        dashboard.setDiscontinuedProducts(
                productRepository.countByStatus(ProductStatus.DISCONTINUED));

        // Appointments
        dashboard.setTotalAppointments(
                appointmentRepository.count());

        dashboard.setBookedAppointments(
                appointmentRepository.countByBookingStatus(
                        BookingStatus.BOOKED
                )
        );

        dashboard.setTodayAppointments(
                appointmentRepository.countByAppointmentDate(LocalDate.now()));

        // Orders
        dashboard.setTotalOrders(
                productOrderRepository.count());

        dashboard.setPendingOrders(
                productOrderRepository.countByPaymentStatus(
                        PaymentStatus.PENDING));

        dashboard.setPaidOrders(
                productOrderRepository.countByPaymentStatus(
                        PaymentStatus.PAID));

        dashboard.setFailedOrders(
                productOrderRepository.countByPaymentStatus(
                        PaymentStatus.FAILED));

        dashboard.setTodayOrders(
                productOrderRepository.countByOrderDate(LocalDate.now()));

        // Revenue
        dashboard.setTotalRevenue(
                productOrderRepository.getTotalRevenue());

        dashboard.setTodayRevenue(
                productOrderRepository.getTodayRevenue(LocalDate.now()));

        return dashboard;
    }

}