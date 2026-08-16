package com.salonmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.enums.TimeSlotStatus;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
import com.salonmanagementsystem.repository.TimeSlotRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentClassService {

    // =========================================
    // REPOSITORIES
    // =========================================

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private SalonRepository salonRepository;


    // =========================================
    // ADD APPOINTMENT
    // =========================================
    @Transactional
    public AppointmentClass addAppointment(
            AppointmentClass appointmentClass) {

        // =====================================
        // FIND SALON
        // =====================================

        if (appointmentClass.getSalon() == null ||
                appointmentClass.getSalon().getId() == null) {

            throw new RuntimeException(
                    "Salon is required");
        }

        Salon salon =
                salonRepository
                        .findById(
                                appointmentClass
                                        .getSalon()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));


        // =====================================
        // FIND SERVICE
        // =====================================

        if (appointmentClass.getSalonService() == null ||
                appointmentClass
                        .getSalonService()
                        .getId() == null) {

            throw new RuntimeException(
                    "Service is required");
        }

        SalonService service =
                serviceRepository
                        .findById(
                                appointmentClass
                                        .getSalonService()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service Not Found"));


        // =====================================
        // CHECK SERVICE → SALON
        // =====================================

        if (service.getSalon() == null ||
                !service.getSalon()
                        .getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Service does not belong "
                    + "to the selected Salon");
        }


        // =====================================
        // FIND EMPLOYEE
        // =====================================

        if (appointmentClass.getEmployee() == null ||
                appointmentClass
                        .getEmployee()
                        .getId() == null) {

            throw new RuntimeException(
                    "Employee is required");
        }

        EmployeeClass employee =
                employeeRepository
                        .findById(
                                appointmentClass
                                        .getEmployee()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));


        // =====================================
        // CHECK EMPLOYEE → SALON
        // =====================================

        if (employee.getSalon() == null ||
                !employee.getSalon()
                        .getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Employee does not belong "
                    + "to the selected Salon");
        }


        // =====================================
        // FIND TIME SLOT
        // =====================================

        if (appointmentClass.getTimeSlot() == null ||
                appointmentClass
                        .getTimeSlot()
                        .getId() == null) {

            throw new RuntimeException(
                    "Time Slot is required");
        }

        TimeSlotClass timeSlot =
                timeSlotRepository
                        .findById(
                                appointmentClass
                                        .getTimeSlot()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Time Slot Not Found"));


        // =====================================
        // CHECK TIME SLOT → SALON
        // =====================================

        if (timeSlot.getSalon() == null ||
                !timeSlot.getSalon()
                        .getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                    + "to the selected Salon");
        }


        // =====================================
        // CHECK TIME SLOT → EMPLOYEE
        // =====================================

        if (timeSlot.getEmployee() == null ||
                !timeSlot.getEmployee()
                        .getId()
                        .equals(employee.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                    + "to the selected Employee");
        }


        // =====================================
        // CHECK TIME SLOT → SERVICE
        // =====================================

        if (timeSlot.getSalonService() == null ||
                !timeSlot.getSalonService()
                        .getId()
                        .equals(service.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                    + "to the selected Service");
        }


        // =====================================
        // CHECK SLOT STATUS
        // =====================================

        if (timeSlot.getSlotStatus()
                == TimeSlotStatus.BOOKED) {

            throw new RuntimeException(
                    "This Time Slot Is Already Booked");
        }


        if (timeSlot.getSlotStatus()
                != TimeSlotStatus.AVAILABLE) {

            throw new RuntimeException(
                    "This Time Slot Is Not Available");
        }


        // =====================================
        // BOOK TIME SLOT
        // =====================================

        timeSlot.setSlotStatus(
                TimeSlotStatus.BOOKED);

        timeSlotRepository.save(timeSlot);


        // =====================================
        // SET VERIFIED RELATIONSHIPS
        // =====================================

        appointmentClass.setSalon(salon);

        appointmentClass.setSalonService(service);

        appointmentClass.setEmployee(employee);

        appointmentClass.setTimeSlot(timeSlot);


        // =====================================
        // SAVE APPOINTMENT
        // =====================================

        return appointmentRepository.save(
                appointmentClass);
    }


    // =========================================
    // GET ALL APPOINTMENTS
    // =========================================

    public List<AppointmentClass>
    getAllAppointments() {

        return appointmentRepository.findAll();
    }


    // =========================================
    // GET APPOINTMENT BY ID
    // =========================================

    public AppointmentClass getAppointmentById(
            Long id) {

        return appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment Not Found"));
    }


    // =========================================
    // UPDATE APPOINTMENT
    // =========================================
    @Transactional
    public AppointmentClass updateAppointment(
            Long id,
            AppointmentClass appointmentClass) {

        // =====================================
        // FIND EXISTING APPOINTMENT
        // =====================================

        AppointmentClass existingAppointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment Not Found"));


        // =====================================
        // FIND SALON
        // =====================================

        if (appointmentClass.getSalon() == null ||
                appointmentClass
                        .getSalon()
                        .getId() == null) {

            throw new RuntimeException(
                    "Salon is required");
        }

        Salon salon =
                salonRepository
                        .findById(
                                appointmentClass
                                        .getSalon()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));


        // =====================================
        // FIND SERVICE
        // =====================================

        SalonService service =
                serviceRepository
                        .findById(
                                appointmentClass
                                        .getSalonService()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Service Not Found"));


        // =====================================
        // CHECK SERVICE → SALON
        // =====================================

        if (service.getSalon() == null ||
                !service.getSalon()
                        .getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Service does not belong "
                    + "to the selected Salon");
        }


        // =====================================
        // FIND EMPLOYEE
        // =====================================

        EmployeeClass employee =
                employeeRepository
                        .findById(
                                appointmentClass
                                        .getEmployee()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));


        // =====================================
        // CHECK EMPLOYEE → SALON
        // =====================================

        if (employee.getSalon() == null ||
                !employee.getSalon()
                        .getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Employee does not belong "
                    + "to the selected Salon");
        }


        // =====================================
        // FIND NEW TIME SLOT
        // =====================================

        TimeSlotClass newSlot =
                timeSlotRepository
                        .findById(
                                appointmentClass
                                        .getTimeSlot()
                                        .getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Time Slot Not Found"));


        // =====================================
        // CHECK NEW SLOT → SALON
        // =====================================

        if (newSlot.getSalon() == null ||
                !newSlot.getSalon()
                        .getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                    + "to the selected Salon");
        }


        // =====================================
        // CHECK NEW SLOT → EMPLOYEE
        // =====================================

        if (newSlot.getEmployee() == null ||
                !newSlot.getEmployee()
                        .getId()
                        .equals(employee.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                    + "to the selected Employee");
        }


        // =====================================
        // CHECK NEW SLOT → SERVICE
        // =====================================

        if (newSlot.getSalonService() == null ||
                !newSlot.getSalonService()
                        .getId()
                        .equals(service.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                    + "to the selected Service");
        }


        // =====================================
        // OLD TIME SLOT
        // =====================================

        TimeSlotClass oldSlot =
                existingAppointment.getTimeSlot();


        // =====================================
        // SLOT CHANGED
        // =====================================

        if (oldSlot != null &&
                !oldSlot.getId()
                        .equals(newSlot.getId())) {

            // Make old slot available

            oldSlot.setSlotStatus(
                    TimeSlotStatus.AVAILABLE);

            timeSlotRepository.save(oldSlot);


            // Check new slot

            if (newSlot.getSlotStatus()
                    == TimeSlotStatus.BOOKED) {

                throw new RuntimeException(
                        "Selected Time Slot Is Already Booked");
            }


            if (newSlot.getSlotStatus()
                    != TimeSlotStatus.AVAILABLE) {

                throw new RuntimeException(
                        "Selected Time Slot Is Not Available");
            }


            // Book new slot

            newSlot.setSlotStatus(
                    TimeSlotStatus.BOOKED);

            timeSlotRepository.save(newSlot);
        }


        // =====================================
        // UPDATE CUSTOMER DETAILS
        // =====================================

        existingAppointment.setCustomerName(
                appointmentClass
                        .getCustomerName());

        existingAppointment.setCustomerPhone(
                appointmentClass
                        .getCustomerPhone());

        existingAppointment.setCustomerEmail(
                appointmentClass
                        .getCustomerEmail());


        // =====================================
        // UPDATE DATE / TIME
        // =====================================

        existingAppointment.setAppointmentDate(
                appointmentClass
                        .getAppointmentDate());

        existingAppointment.setAppointmentTime(
                appointmentClass
                        .getAppointmentTime());


        // =====================================
        // UPDATE STATUS
        // =====================================

        existingAppointment.setBookingStatus(
                appointmentClass
                        .getBookingStatus());

        existingAppointment.setPaymentStatus(
                appointmentClass
                        .getPaymentStatus());


        // =====================================
        // UPDATE RELATIONSHIPS
        // =====================================

        existingAppointment.setSalon(
                salon);

        existingAppointment.setSalonService(
                service);

        existingAppointment.setEmployee(
                employee);

        existingAppointment.setTimeSlot(
                newSlot);


        // =====================================
        // SAVE
        // =====================================

        return appointmentRepository.save(
                existingAppointment);
    }


    // =========================================
    // DELETE APPOINTMENT
    // =========================================
    @Transactional
    public String deleteAppointment(
            Long id) {

        AppointmentClass appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment Not Found"));


        // =====================================
        // RELEASE TIME SLOT
        // =====================================

        if (appointment.getTimeSlot() != null) {

            TimeSlotClass slot =
                    appointment.getTimeSlot();

            slot.setSlotStatus(
                    TimeSlotStatus.AVAILABLE);

            timeSlotRepository.save(slot);
        }


        // =====================================
        // DELETE APPOINTMENT
        // =====================================

        appointmentRepository.delete(
                appointment);

        return "Appointment Deleted Successfully";
    }


    // =========================================
    // SEARCH APPOINTMENTS
    // =========================================

    public List<AppointmentClass>
    searchAppointments(
            String customerName) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE);

        return appointmentRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName,
                        pageable)
                .getContent();
    }


    // =========================================
    // PAGINATION
    // =========================================

    public PaginationResponse<AppointmentClass>
    getAppointmentsPagination(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size);

        Page<AppointmentClass> appointmentPage =
                appointmentRepository
                        .findAll(pageable);

        return new PaginationResponse<>(

                appointmentPage.getContent(),

                appointmentPage.getNumber(),

                appointmentPage.getTotalPages(),

                appointmentPage.getTotalElements(),

                appointmentPage.getSize()
        );
    }


    // =========================================
    // FILTER BY BOOKING STATUS
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByBookingStatus(
            BookingStatus bookingStatus) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE);

        return appointmentRepository
                .findByBookingStatus(
                        bookingStatus,
                        pageable)
                .getContent();
    }


    // =========================================
    // FILTER BY PAYMENT STATUS
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByPaymentStatus(
            PaymentStatus paymentStatus) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE);

        return appointmentRepository
                .findByPaymentStatus(
                        paymentStatus,
                        pageable)
                .getContent();
    }


    // =========================================
    // FILTER BY DATE
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByDate(
            LocalDate date) {

        return appointmentRepository
                .findByAppointmentDate(date);
    }


    // =========================================
    // FILTER BY EMPLOYEE
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByEmployee(
            Long employeeId) {

        return appointmentRepository
                .findByEmployeeId(employeeId);
    }
}