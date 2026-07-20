package com.salonmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.entity.DashboardClass;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.BillRepository;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.ProductRepository;
import com.salonmanagementsystem.repository.ServiceRepository;

@Service
public class DashboardClassService {

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

    @Autowired
    private BillRepository billRepository;

    public DashboardClass getDashboard() {

        DashboardClass dashboard = new DashboardClass();

        dashboard.setTotalEmployees(employeeRepository.count());

        dashboard.setTotalServices(serviceRepository.count());

        dashboard.setTotalProducts(productRepository.count());

        dashboard.setAvailableProducts(
                productRepository.findAll()
                        .stream()
                        .filter(product -> product.getStockQuantity() > 0)
                        .count()
        );

        dashboard.setTotalAppointments(
                appointmentRepository.count()
        );

        dashboard.setBookedAppointments(
                appointmentRepository.findAll()
                        .stream()
                        .filter(a -> a.getBookingStatus()
                                .equalsIgnoreCase("BOOKED"))
                        .count()
        );

        dashboard.setTotalOrders(
                productOrderRepository.count()
        );

        List<BillClass> bills = billRepository.findAll();

        double revenue = bills.stream()
                .mapToDouble(BillClass::getTotalAmount)
                .sum();

        dashboard.setTotalRevenue(revenue);

        LocalDate today = LocalDate.now();

        long todayAppointments = appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getAppointmentDate().equals(today))
                .count();

        dashboard.setTodayAppointments(todayAppointments);

        double todayRevenue = bills.stream()
                .filter(b -> b.getBillDate().equals(today))
                .mapToDouble(BillClass::getTotalAmount)
                .sum();

        dashboard.setTodayRevenue(todayRevenue);

        return dashboard;
    }

}