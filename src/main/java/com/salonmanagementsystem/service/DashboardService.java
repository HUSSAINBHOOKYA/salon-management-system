package com.salonmanagementsystem.service;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.LinkedHashMap;

import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.DashboardResponse;

import com.salonmanagementsystem.dto.PartnerReportResponse;

import com.salonmanagementsystem.dto.PartnerReportResponse.EmployeePerformance;

import com.salonmanagementsystem.dto.PartnerReportResponse.ProductPerformance;

import com.salonmanagementsystem.dto.PartnerReportResponse.ServicePerformance;

import com.salonmanagementsystem.entity.AppointmentClass;

import com.salonmanagementsystem.entity.EmployeeClass;

import com.salonmanagementsystem.entity.ProductClass;

import com.salonmanagementsystem.entity.ProductOrderClass;

import com.salonmanagementsystem.entity.Salon;

import com.salonmanagementsystem.entity.SalonService;

import com.salonmanagementsystem.enums.BookingStatus;

import com.salonmanagementsystem.enums.PaymentStatus;

import com.salonmanagementsystem.enums.ProductStatus;

import com.salonmanagementsystem.repository.AppointmentRepository;

import com.salonmanagementsystem.repository.CategoryRepository;

import com.salonmanagementsystem.repository.EmployeeRepository;

import com.salonmanagementsystem.repository.ProductOrderRepository;

import com.salonmanagementsystem.repository.ProductRepository;

import com.salonmanagementsystem.repository.SalonRepository;

import com.salonmanagementsystem.repository.ServiceRepository;


@Service
public class DashboardService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private SalonRepository salonRepository;


    // =========================================
    // ADMIN / GLOBAL DASHBOARD
    // =========================================

    public DashboardResponse getDashboard() {

        DashboardResponse dashboard =
                new DashboardResponse();


        // =========================================
        // CATEGORY
        // =========================================

        dashboard.setTotalCategories(
                categoryRepository.count()
        );


        // =========================================
        // EMPLOYEE
        // =========================================

        dashboard.setTotalEmployees(
                employeeRepository.count()
        );


        // =========================================
        // SERVICES
        // =========================================

        dashboard.setTotalServices(
                serviceRepository.count()
        );


        // =========================================
        // PRODUCTS
        // =========================================

        dashboard.setTotalProducts(
                productRepository.count()
        );

        dashboard.setAvailableProducts(
                productRepository.countByStatus(
                        ProductStatus.AVAILABLE
                )
        );

        dashboard.setOutOfStockProducts(
                productRepository.countByStatus(
                        ProductStatus.OUT_OF_STOCK
                )
        );

        dashboard.setDiscontinuedProducts(
                productRepository.countByStatus(
                        ProductStatus.DISCONTINUED
                )
        );


        // =========================================
        // APPOINTMENTS
        // =========================================

        dashboard.setTotalAppointments(
                appointmentRepository.count()
        );

        dashboard.setBookedAppointments(
                appointmentRepository.countByBookingStatus(
                        BookingStatus.BOOKED
                )
        );

        dashboard.setTodayAppointments(
                appointmentRepository.countByAppointmentDate(
                        LocalDate.now()
                )
        );


        // =========================================
        // ORDERS
        // =========================================

        dashboard.setTotalOrders(
                productOrderRepository.count()
        );

        dashboard.setPendingOrders(
                productOrderRepository.countByPaymentStatus(
                        PaymentStatus.PENDING
                )
        );

        dashboard.setPaidOrders(
                productOrderRepository.countByPaymentStatus(
                        PaymentStatus.PAID
                )
        );

        dashboard.setFailedOrders(
                productOrderRepository.countByPaymentStatus(
                        PaymentStatus.FAILED
                )
        );

        dashboard.setTodayOrders(
                productOrderRepository.countByOrderDate(
                        LocalDate.now()
                )
        );


        // =========================================
        // REVENUE
        // =========================================

        dashboard.setTotalRevenue(
                productOrderRepository.getTotalRevenue()
        );

        dashboard.setTodayRevenue(
                productOrderRepository.getTodayRevenue(
                        LocalDate.now()
                )
        );


        return dashboard;
    }


    // =========================================
    // PARTNER REPORTS
    // =========================================

    @Transactional(readOnly = true)
    public PartnerReportResponse getPartnerReports(
            String partnerEmail) {


        // =========================================
        // FIND PARTNER SALON
        // =========================================

        Salon partnerSalon =
                salonRepository
                        .findByEmail(partnerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Partner salon not found"
                                )
                        );


        Long salonId =
                partnerSalon.getId();


        // =========================================
        // EMPLOYEES OF THIS SALON
        // =========================================

        List<EmployeeClass> employees =
                employeeRepository
                        .findAll()
                        .stream()
                        .filter(employee ->
                                employee.getSalon() != null
                                &&
                                employee.getSalon()
                                        .getId()
                                        .equals(salonId)
                        )
                        .collect(Collectors.toList());


        // =========================================
        // SERVICES OF THIS SALON
        // =========================================

        List<SalonService> services =
                serviceRepository
                        .findAll()
                        .stream()
                        .filter(service ->
                                service.getSalon() != null
                                &&
                                service.getSalon()
                                        .getId()
                                        .equals(salonId)
                        )
                        .collect(Collectors.toList());


        // =========================================
        // PRODUCTS OF THIS SALON
        // =========================================

        List<ProductClass> products =
                productRepository
                        .findAll()
                        .stream()
                        .filter(product ->
                                product.getSalon() != null
                                &&
                                product.getSalon()
                                        .getId()
                                        .equals(salonId)
                        )
                        .collect(Collectors.toList());


        // =========================================
        // APPOINTMENTS OF THIS SALON
        // =========================================

        List<AppointmentClass> appointments =
                appointmentRepository
                        .findAll()
                        .stream()
                        .filter(appointment ->
                                appointment.getSalon() != null
                                &&
                                appointment.getSalon()
                                        .getId()
                                        .equals(salonId)
                        )
                        .collect(Collectors.toList());


        // =========================================
        // ORDERS OF THIS SALON
        // =========================================

        List<ProductOrderClass> orders =
                productOrderRepository
                        .findAll()
                        .stream()
                        .filter(order ->
                                order.getProduct() != null
                                &&
                                order.getProduct().getSalon() != null
                                &&
                                order.getProduct()
                                        .getSalon()
                                        .getId()
                                        .equals(salonId)
                        )
                        .collect(Collectors.toList());


        // =========================================
        // SUMMARY
        // =========================================

        long totalEmployees =
                employees.size();

        long totalServices =
                services.size();

        long totalProducts =
                products.size();

        long totalAppointments =
                appointments.size();

        long totalOrders =
                orders.size();


        // =========================================
        // UNIQUE CUSTOMERS
        // =========================================

        long totalCustomers =
                appointments
                        .stream()
                        .map(AppointmentClass::getCustomerEmail)
                        .filter(email ->
                                email != null
                                &&
                                !email.isBlank()
                        )
                        .distinct()
                        .count();


        // =========================================
        // APPOINTMENT STATUS
        // =========================================

        long bookedAppointments =
                appointments
                        .stream()
                        .filter(appointment ->
                                appointment.getBookingStatus()
                                        == BookingStatus.BOOKED
                        )
                        .count();


        long completedAppointments =
                appointments
                        .stream()
                        .filter(appointment ->
                                appointment.getBookingStatus()
                                        == BookingStatus.COMPLETED
                        )
                        .count();


        long cancelledAppointments =
                appointments
                        .stream()
                        .filter(appointment ->
                                appointment.getBookingStatus()
                                        == BookingStatus.CANCELLED
                        )
                        .count();


        long todayAppointments =
                appointments
                        .stream()
                        .filter(appointment ->
                                LocalDate.now().equals(
                                        appointment.getAppointmentDate()
                                )
                        )
                        .count();


        // =========================================
        // PRODUCT STATUS
        // =========================================

        long availableProducts =
                products
                        .stream()
                        .filter(product ->
                                product.getStatus()
                                        == ProductStatus.AVAILABLE
                        )
                        .count();


        long outOfStockProducts =
                products
                        .stream()
                        .filter(product ->
                                product.getStatus()
                                        == ProductStatus.OUT_OF_STOCK
                        )
                        .count();


        long discontinuedProducts =
                products
                        .stream()
                        .filter(product ->
                                product.getStatus()
                                        == ProductStatus.DISCONTINUED
                        )
                        .count();


        // =========================================
        // ORDER STATUS
        // =========================================

        long pendingOrders =
                orders
                        .stream()
                        .filter(order ->
                                order.getPaymentStatus()
                                        == PaymentStatus.PENDING
                        )
                        .count();


        long paidOrders =
                orders
                        .stream()
                        .filter(order ->
                                order.getPaymentStatus()
                                        == PaymentStatus.PAID
                        )
                        .count();


        long failedOrders =
                orders
                        .stream()
                        .filter(order ->
                                order.getPaymentStatus()
                                        == PaymentStatus.FAILED
                        )
                        .count();


        // =========================================
        // APPOINTMENT REVENUE
        // =========================================

        double appointmentRevenue =
                appointments
                        .stream()
                        .filter(appointment ->
                                appointment.getBookingStatus()
                                        != BookingStatus.CANCELLED
                        )
                        .filter(appointment ->
                                appointment.getTotalPrice() != null
                        )
                        .mapToDouble(
                                AppointmentClass::getTotalPrice
                        )
                        .sum();


        // =========================================
        // PRODUCT REVENUE
        // =========================================

        double productRevenue =
                orders
                        .stream()
                        .filter(order ->
                                order.getPaymentStatus()
                                        != PaymentStatus.FAILED
                        )
                        .filter(order ->
                                order.getTotalPrice() != null
                        )
                        .mapToDouble(
                                ProductOrderClass::getTotalPrice
                        )
                        .sum();


        // =========================================
        // TOTAL REVENUE
        // =========================================

        double totalRevenue =
                appointmentRevenue +
                productRevenue;


        // =========================================
        // TODAY REVENUE
        // =========================================

        double todayAppointmentRevenue =
                appointments
                        .stream()
                        .filter(appointment ->
                                LocalDate.now().equals(
                                        appointment.getAppointmentDate()
                                )
                        )
                        .filter(appointment ->
                                appointment.getBookingStatus()
                                        != BookingStatus.CANCELLED
                        )
                        .filter(appointment ->
                                appointment.getTotalPrice() != null
                        )
                        .mapToDouble(
                                AppointmentClass::getTotalPrice
                        )
                        .sum();


        double todayProductRevenue =
                orders
                        .stream()
                        .filter(order ->
                                LocalDate.now().equals(
                                        order.getOrderDate()
                                )
                        )
                        .filter(order ->
                                order.getPaymentStatus()
                                        != PaymentStatus.FAILED
                        )
                        .filter(order ->
                                order.getTotalPrice() != null
                        )
                        .mapToDouble(
                                ProductOrderClass::getTotalPrice
                        )
                        .sum();


        double todayRevenue =
                todayAppointmentRevenue +
                todayProductRevenue;


        // =========================================
        // SERVICE PERFORMANCE
        // =========================================

        Map<Long, ServicePerformance>
                serviceMap =
                new LinkedHashMap<>();


        for (AppointmentClass appointment :
                appointments) {

            if (appointment.getBookingStatus()
                    == BookingStatus.CANCELLED) {

                continue;
            }

            if (appointment.getServices() == null) {

                continue;
            }


            for (SalonService service :
                    appointment.getServices()) {

                if (service == null ||
                        service.getId() == null) {

                    continue;
                }


                ServicePerformance performance =
                        serviceMap.computeIfAbsent(
                                service.getId(),
                                id ->
                                        new ServicePerformance(
                                                id,
                                                service.getServiceName(),
                                                0,
                                                0.0
                                        )
                        );


                performance.setBookingCount(
                        performance.getBookingCount() + 1
                );


                double servicePrice =
                        service.getPrice() != null
                                ? service.getPrice()
                                : 0.0;


                performance.setRevenue(
                        performance.getRevenue()
                                + servicePrice
                );
            }
        }


        List<ServicePerformance>
                servicePerformance =
                new ArrayList<>(
                        serviceMap.values()
                );


        servicePerformance.sort(
                Comparator
                        .comparingLong(
                                ServicePerformance::getBookingCount
                        )
                        .reversed()
        );


        // =========================================
        // EMPLOYEE PERFORMANCE
        // =========================================

        Map<Long, EmployeePerformance>
                employeeMap =
                new LinkedHashMap<>();


        for (AppointmentClass appointment :
                appointments) {

            if (appointment.getBookingStatus()
                    == BookingStatus.CANCELLED) {

                continue;
            }

            if (appointment.getEmployee() == null ||
                    appointment.getEmployee().getId() == null) {

                continue;
            }


            EmployeeClass employee =
                    appointment.getEmployee();


            EmployeePerformance performance =
                    employeeMap.computeIfAbsent(
                            employee.getId(),
                            id ->
                                    new EmployeePerformance(
                                            id,
                                            employee.getEmployeeName(),
                                            0,
                                            0.0
                                    )
                    );


            performance.setAppointmentCount(
                    performance.getAppointmentCount() + 1
            );


            double revenue =
                    appointment.getTotalPrice() != null
                            ? appointment.getTotalPrice()
                            : 0.0;


            performance.setRevenue(
                    performance.getRevenue() + revenue
            );
        }


        List<EmployeePerformance>
                employeePerformance =
                new ArrayList<>(
                        employeeMap.values()
                );


        employeePerformance.sort(
                Comparator
                        .comparingLong(
                                EmployeePerformance::getAppointmentCount
                        )
                        .reversed()
        );


        // =========================================
        // PRODUCT PERFORMANCE
        // =========================================

        Map<Long, ProductPerformance>
                productMap =
                new LinkedHashMap<>();


        for (ProductOrderClass order :
                orders) {

            if (order.getProduct() == null ||
                    order.getProduct().getId() == null) {

                continue;
            }

            if (order.getPaymentStatus()
                    == PaymentStatus.FAILED) {

                continue;
            }


            ProductClass product =
                    order.getProduct();


            ProductPerformance performance =
                    productMap.computeIfAbsent(
                            product.getId(),
                            id ->
                                    new ProductPerformance(
                                            id,
                                            product.getProductName(),
                                            0,
                                            0.0,
                                            0
                                    )
                    );


            long quantity =
                    order.getQuantity() != null
                            ? order.getQuantity()
                            : 0;


            double revenue =
                    order.getTotalPrice() != null
                            ? order.getTotalPrice()
                            : 0.0;


            performance.setQuantitySold(
                    performance.getQuantitySold()
                            + quantity
            );


            performance.setRevenue(
                    performance.getRevenue()
                            + revenue
            );


            performance.setOrderCount(
                    performance.getOrderCount()
                            + 1
            );
        }


        List<ProductPerformance>
                productPerformance =
                new ArrayList<>(
                        productMap.values()
                );


        productPerformance.sort(
                Comparator
                        .comparingLong(
                                ProductPerformance::getQuantitySold
                        )
                        .reversed()
        );


        // =========================================
        // BUILD RESPONSE
        // =========================================

        PartnerReportResponse response =
                new PartnerReportResponse();


        response.setSalonId(
                salonId
        );

        response.setSalonName(
                partnerSalon.getSalonName()
        );


        response.setTotalEmployees(
                totalEmployees
        );

        response.setTotalServices(
                totalServices
        );

        response.setTotalProducts(
                totalProducts
        );

        response.setTotalAppointments(
                totalAppointments
        );

        response.setTotalOrders(
                totalOrders
        );

        response.setTotalCustomers(
                totalCustomers
        );


        response.setBookedAppointments(
                bookedAppointments
        );

        response.setCompletedAppointments(
                completedAppointments
        );

        response.setCancelledAppointments(
                cancelledAppointments
        );

        response.setTodayAppointments(
                todayAppointments
        );


        response.setAvailableProducts(
                availableProducts
        );

        response.setOutOfStockProducts(
                outOfStockProducts
        );

        response.setDiscontinuedProducts(
                discontinuedProducts
        );


        response.setPendingOrders(
                pendingOrders
        );

        response.setPaidOrders(
                paidOrders
        );

        response.setFailedOrders(
                failedOrders
        );


        response.setAppointmentRevenue(
                appointmentRevenue
        );

        response.setProductRevenue(
                productRevenue
        );

        response.setTotalRevenue(
                totalRevenue
        );

        response.setTodayRevenue(
                todayRevenue
        );


        response.setServicePerformance(
                servicePerformance
        );

        response.setEmployeePerformance(
                employeePerformance
        );

        response.setProductPerformance(
                productPerformance
        );


        return response;
    }

}