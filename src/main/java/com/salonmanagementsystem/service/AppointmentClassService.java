package com.salonmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.ServiceClass;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
import com.salonmanagementsystem.repository.TimeSlotRepository;

@Service
public class AppointmentClassService {


    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;



    // Add Appointment
    public AppointmentClass addAppointment(AppointmentClass appointmentClass) {


        // Find Service
        ServiceClass service = serviceRepository
                .findById(appointmentClass.getService().getId())
                .orElseThrow(() ->
                new RuntimeException("Service Not Found"));


        // Find Employee
        EmployeeClass employee = employeeRepository
                .findById(appointmentClass.getEmployee().getId())
                .orElseThrow(() ->
                new RuntimeException("Employee Not Found"));



        // Find Time Slot
        TimeSlotClass timeSlot = timeSlotRepository
                .findById(appointmentClass.getTimeSlot().getId())
                .orElseThrow(() ->
                new RuntimeException("Time Slot Not Found"));



        // Check Slot Available
        if(timeSlot.getSlotStatus()
                .equalsIgnoreCase("BOOKED")) {

            throw new RuntimeException(
                    "This time slot is already booked."
            );
        }



        // Change Slot Status
        timeSlot.setSlotStatus("BOOKED");

        timeSlotRepository.save(timeSlot);



        // Set Complete Objects

        appointmentClass.setService(service);

        appointmentClass.setEmployee(employee);

        appointmentClass.setTimeSlot(timeSlot);



        return appointmentRepository.save(appointmentClass);
    }




    // Get All Appointments
    public List<AppointmentClass> getAllAppointments() {

        return appointmentRepository.findAll();
    }





    // Get Appointment By Id
    public AppointmentClass getAppointmentById(Long id) {

        return appointmentRepository.findById(id)
                .orElse(null);
    }





    // Update Appointment
    public AppointmentClass updateAppointment(Long id,
                                             AppointmentClass appointmentClass) {


        AppointmentClass existingAppointment =
                appointmentRepository.findById(id)
                .orElseThrow(() ->
                new RuntimeException("Appointment Not Found"));



        ServiceClass service = serviceRepository
                .findById(appointmentClass.getService().getId())
                .orElseThrow(() ->
                new RuntimeException("Service Not Found"));



        EmployeeClass employee = employeeRepository
                .findById(appointmentClass.getEmployee().getId())
                .orElseThrow(() ->
                new RuntimeException("Employee Not Found"));



        TimeSlotClass timeSlot = timeSlotRepository
                .findById(appointmentClass.getTimeSlot().getId())
                .orElseThrow(() ->
                new RuntimeException("Time Slot Not Found"));



        existingAppointment.setCustomerName(
                appointmentClass.getCustomerName());

        existingAppointment.setCustomerPhone(
                appointmentClass.getCustomerPhone());

        existingAppointment.setCustomerEmail(
                appointmentClass.getCustomerEmail());

        existingAppointment.setAppointmentDate(
                appointmentClass.getAppointmentDate());

        existingAppointment.setAppointmentTime(
                appointmentClass.getAppointmentTime());

        existingAppointment.setBookingStatus(
                appointmentClass.getBookingStatus());

        existingAppointment.setPaymentStatus(
                appointmentClass.getPaymentStatus());



        existingAppointment.setService(service);

        existingAppointment.setEmployee(employee);

        existingAppointment.setTimeSlot(timeSlot);



        return appointmentRepository.save(existingAppointment);
    }





    // Delete Appointment
    public String deleteAppointment(Long id) {


        AppointmentClass appointment =
                appointmentRepository.findById(id)
                .orElse(null);


        if(appointment != null) {


            // Make slot available again
            if(appointment.getTimeSlot() != null) {

                TimeSlotClass timeSlot =
                        appointment.getTimeSlot();

                timeSlot.setSlotStatus("AVAILABLE");

                timeSlotRepository.save(timeSlot);
            }


            appointmentRepository.deleteById(id);


            return "Appointment Deleted Successfully";
        }


        return "Appointment Not Found";
    }
    
    public List<AppointmentClass> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDate(date);
    }
    public List<AppointmentClass> getAppointmentsByEmployee(Long employeeId) {
        return appointmentRepository.findByEmployeeId(employeeId);
    }

}













//package UseProjectAuthenticationandAuthorization.simple.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import UseProjectAuthenticationandAuthorization.simple.entity.AppointmentClass;
//import UseProjectAuthenticationandAuthorization.simple.entity.EmployeeClass;
//import UseProjectAuthenticationandAuthorization.simple.entity.ServiceClass;
//import UseProjectAuthenticationandAuthorization.simple.entity.TimeSlotClass;
//import UseProjectAuthenticationandAuthorization.simple.repository.AppointmentRepository;
//import UseProjectAuthenticationandAuthorization.simple.repository.EmployeeRepository;
//import UseProjectAuthenticationandAuthorization.simple.repository.ServiceRepository;
//import UseProjectAuthenticationandAuthorization.simple.repository.TimeSlotRepository;
//
//@Service
//public class AppointmentClassService {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private ServiceRepository serviceRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//    @Autowired
//    private TimeSlotRepository timeSlotRepository;
//
//    // Add Appointment
//    
//    public AppointmentClass addAppointment(AppointmentClass appointmentClass) {
//
//
//        TimeSlotClass timeSlot = timeSlotRepository
//                .findById(appointmentClass.getTimeSlot().getId())
//                .orElseThrow(() -> 
//                new RuntimeException("Time Slot Not Found"));


        // Check Slot Status

//        if(timeSlot.getSlotStatus()
//                .equalsIgnoreCase("BOOKED")) {
//
//            throw new RuntimeException(
//                    "This time slot is already booked."
//            );
//        }


        // Change Slot Status

//        timeSlot.setSlotStatus("BOOKED");
//
//        timeSlotRepository.save(timeSlot);


        // Save Appointment
//
//        appointmentClass.setTimeSlot(timeSlot);
//
//
//        return appointmentRepository.save(appointmentClass);
//    }
//    public AppointmentClass addAppointment(AppointmentClass appointmentClass) {
//
//        ServiceClass service = serviceRepository
//                .findById(appointmentClass.getService().getId())
//                .orElseThrow(() -> new RuntimeException("Service Not Found"));
//
//        EmployeeClass employee = employeeRepository
//                .findById(appointmentClass.getEmployee().getId())
//                .orElseThrow(() -> new RuntimeException("Employee Not Found"));
//
//        appointmentClass.setService(service);
//        appointmentClass.setEmployee(employee);
//
//        return appointmentRepository.save(appointmentClass);
//    }

    // Get All Appointments
//    public List<AppointmentClass> getAllAppointments() {
//        return appointmentRepository.findAll();
//    }
//
//    // Get Appointment By Id
//    public AppointmentClass getAppointmentById(Long id) {
//        return appointmentRepository.findById(id).orElse(null);
//    }
//
//    // Update Appointment
//    public AppointmentClass updateAppointment(Long id, AppointmentClass appointmentClass) {
//
//        AppointmentClass existingAppointment = appointmentRepository.findById(id).orElse(null);
//
//        if (existingAppointment != null) {
//
//            ServiceClass service = serviceRepository
//                    .findById(appointmentClass.getService().getId())
//                    .orElseThrow(() -> new RuntimeException("Service Not Found"));
//
//            EmployeeClass employee = employeeRepository
//                    .findById(appointmentClass.getEmployee().getId())
//                    .orElseThrow(() -> new RuntimeException("Employee Not Found"));
//
//            existingAppointment.setCustomerName(appointmentClass.getCustomerName());
//            existingAppointment.setCustomerPhone(appointmentClass.getCustomerPhone());
//            existingAppointment.setCustomerEmail(appointmentClass.getCustomerEmail());
//            existingAppointment.setAppointmentDate(appointmentClass.getAppointmentDate());
//            existingAppointment.setAppointmentTime(appointmentClass.getAppointmentTime());
//            existingAppointment.setBookingStatus(appointmentClass.getBookingStatus());
//            existingAppointment.setPaymentStatus(appointmentClass.getPaymentStatus());
//
//            existingAppointment.setService(service);
//            existingAppointment.setEmployee(employee);
//
//            return appointmentRepository.save(existingAppointment);
//        }
//
//        return null;
//    }
//
//    // Delete Appointment
//    public String deleteAppointment(Long id) {
//
//        if (appointmentRepository.existsById(id)) {
//            appointmentRepository.deleteById(id);
//            return "Appointment Deleted Successfully";
//        }
//
//        return "Appointment Not Found";
//    }
//}


































//package UseProjectAuthenticationandAuthorization.simple.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import UseProjectAuthenticationandAuthorization.simple.entity.AppointmentClass;
//import UseProjectAuthenticationandAuthorization.simple.repository.AppointmentRepository;
//
//@Service
//public class AppointmentClassService {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    // Add Appointment
//    public AppointmentClass addAppointment(AppointmentClass appointmentClass) {
//        return appointmentRepository.save(appointmentClass);
//    }
//
//    // Get All Appointments
//    public List<AppointmentClass> getAllAppointments() {
//        return appointmentRepository.findAll();
//    }
//
//    // Get Appointment By Id
//    public AppointmentClass getAppointmentById(Long id) {
//        return appointmentRepository.findById(id).orElse(null);
//    }
//
//    // Update Appointment
//    public AppointmentClass updateAppointment(Long id, AppointmentClass appointmentClass) {
//
//        AppointmentClass existingAppointment = appointmentRepository.findById(id).orElse(null);
//
//        if (existingAppointment != null) {
//
//            existingAppointment.setCustomerName(appointmentClass.getCustomerName());
//            existingAppointment.setCustomerPhone(appointmentClass.getCustomerPhone());
//            existingAppointment.setCustomerEmail(appointmentClass.getCustomerEmail());
//            existingAppointment.setService(appointmentClass.getService());
//            existingAppointment.setService(appointmentClass.getService());
//            existingAppointment.setAppointmentDate(appointmentClass.getAppointmentDate());
//            existingAppointment.setAppointmentTime(appointmentClass.getAppointmentTime());
//            existingAppointment.setBookingStatus(appointmentClass.getBookingStatus());
//            existingAppointment.setPaymentStatus(appointmentClass.getPaymentStatus());
//
//            return appointmentRepository.save(existingAppointment);
//        }
//
//        return null;
//    }
//
//    // Delete Appointment
//    public String deleteAppointment(Long id) {
//
//        if (appointmentRepository.existsById(id)) {
//            appointmentRepository.deleteById(id);
//            return "Appointment Deleted Successfully";
//        }
//
//        return "Appointment Not Found";
//    }
//
//}