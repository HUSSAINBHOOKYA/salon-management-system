package com.salonmanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.AppointmentRequestDTO;
import com.salonmanagementsystem.dto.AppointmentResponseDTO;
import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.service.AppointmentClassService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentClassController {

    @Autowired
    private AppointmentClassService appointmentClassService;


    // =========================================
    // ADD APPOINTMENT
    // CUSTOMER BOOKING
    // =========================================

    @PostMapping("/add")
    public AppointmentClass addAppointment(
            @RequestBody AppointmentRequestDTO request,
            Authentication authentication) {

        /*
         * IMPORTANT:
         *
         * The logged-in user's email is the real owner
         * of this appointment.
         *
         * Do NOT trust customerEmail coming from frontend.
         */

        String customerEmail =
                authentication.getName();

        request.setCustomerEmail(
                customerEmail
        );

        return appointmentClassService.addAppointment(
                request
        );
    }


    // =========================================
    // GET ALL APPOINTMENTS
    // ADMIN
    // =========================================

    @GetMapping("/all")
    public List<AppointmentResponseDTO> getAllAppointments() {

        List<AppointmentClass> appointments =
                appointmentClassService.getAllAppointments();

        return appointments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    // =========================================
    // GET MY APPOINTMENTS
    // LOGGED-IN CUSTOMER
    // =========================================

    @GetMapping("/my")
    public List<AppointmentResponseDTO> getMyAppointments(
            Authentication authentication) {

        String customerEmail =
                authentication.getName();

        List<AppointmentClass> appointments =
                appointmentClassService.getMyAppointments(
                        customerEmail
                );

        return appointments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    // =========================================
    // CONVERT APPOINTMENT TO RESPONSE DTO
    // =========================================

    private AppointmentResponseDTO convertToResponseDTO(
            AppointmentClass appointment) {

        // =====================================
        // SERVICE NAME
        // =====================================

        String serviceName = "";

        /*
         * MULTIPLE SERVICES
         */

        if (appointment.getServices() != null
                && !appointment.getServices().isEmpty()) {

            serviceName =
                    appointment.getServices()
                            .stream()
                            .map(service ->
                                    service.getServiceName())
                            .collect(Collectors.joining(", "));
        }


        /*
         * OLD SINGLE SERVICE COMPATIBILITY
         */

        if (serviceName.isEmpty()
                && appointment.getSalonService() != null) {

            serviceName =
                    appointment
                            .getSalonService()
                            .getServiceName();
        }


        // =====================================
        // EMPLOYEE
        // =====================================

        String employeeName = null;

        String employeeImage = null;

        if (appointment.getEmployee() != null) {

            employeeName =
                    appointment
                            .getEmployee()
                            .getEmployeeName();

            employeeImage =
                    appointment
                            .getEmployee()
                            .getImageUrl();
        }


        // =====================================
        // SALON
        // =====================================

        Long salonId = null;

        String salonName = null;

        if (appointment.getSalon() != null) {

            salonId =
                    appointment
                            .getSalon()
                            .getId();

            salonName =
                    appointment
                            .getSalon()
                            .getSalonName();
        }


        // =====================================
        // RESPONSE DTO
        // =====================================

        return new AppointmentResponseDTO(

                appointment.getId(),

                appointment.getCustomerName(),

                appointment.getCustomerPhone(),

                appointment.getCustomerEmail(),

                serviceName,

                employeeName,

                employeeImage,

                salonId,

                salonName,

                appointment.getAppointmentDate(),

                appointment.getAppointmentTime(),

                appointment.getBookingStatus() != null
                        ? appointment
                                .getBookingStatus()
                                .name()
                        : null,

                appointment.getPaymentStatus() != null
                        ? appointment
                                .getPaymentStatus()
                                .name()
                        : null,

                appointment.getTotalPrice(),

                appointment.getTotalDuration()
        );
    }


    // =========================================
    // GET APPOINTMENT BY ID
    // =========================================

    @GetMapping("/{id}")
    public AppointmentClass getAppointmentById(
            @PathVariable Long id) {

        return appointmentClassService
                .getAppointmentById(id);
    }


    // =========================================
    // UPDATE APPOINTMENT
    // =========================================
    //
    // IMPORTANT:
    // Return AppointmentResponseDTO instead of
    // AppointmentClass.
    //
    // This prevents Hibernate/Jackson from trying
    // to serialize the complete entity relationships.
    // =========================================

    @PutMapping("/update/{id}")
    public AppointmentResponseDTO updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentClass appointmentClass) {

        AppointmentClass updatedAppointment =
                appointmentClassService.updateAppointment(
                        id,
                        appointmentClass
                );

        return convertToResponseDTO(
                updatedAppointment
        );
    }


    // =========================================
    // DELETE APPOINTMENT
    // =========================================

    @DeleteMapping("/delete/{id}")
    public String deleteAppointment(
            @PathVariable Long id) {

        return appointmentClassService
                .deleteAppointment(id);
    }


    // =========================================
    // SEARCH APPOINTMENT
    // =========================================

    @GetMapping("/search")
    public List<AppointmentClass> searchAppointments(
            @RequestParam String customer) {

        return appointmentClassService
                .searchAppointments(customer);
    }


    // =========================================
    // PAGINATION
    // =========================================

    @GetMapping("/pagination")
    public PaginationResponse<AppointmentClass>
            getAppointmentPagination(
                    @RequestParam(
                            defaultValue = "0"
                    )
                    int page,

                    @RequestParam(
                            defaultValue = "10"
                    )
                    int size) {

        return appointmentClassService
                .getAppointmentsPagination(
                        page,
                        size
                );
    }


    // =========================================
    // FILTER BY BOOKING STATUS
    // =========================================

    @GetMapping("/status/{status}")
    public List<AppointmentClass>
            getAppointmentsByStatus(
                    @PathVariable BookingStatus status) {

        return appointmentClassService
                .getAppointmentsByBookingStatus(
                        status
                );
    }


    // =========================================
    // FILTER BY DATE
    // =========================================

    @GetMapping("/date/{date}")
    public List<AppointmentClass> getAppointmentsByDate(
            @PathVariable LocalDate date) {

        return appointmentClassService
                .getAppointmentsByDate(date);
    }


    // =========================================
    // FILTER BY EMPLOYEE
    // =========================================

    @GetMapping("/employee/{id}")
    public List<AppointmentClass> getAppointmentsByEmployee(
            @PathVariable Long id) {

        return appointmentClassService
                .getAppointmentsByEmployee(id);
    }
}