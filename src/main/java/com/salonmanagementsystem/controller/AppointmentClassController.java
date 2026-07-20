package com.salonmanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.service.AppointmentClassService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentClassController {

    @Autowired
    private AppointmentClassService appointmentClassService;

    // Add Appointment
    @PostMapping("/add")
    public AppointmentClass addAppointment(@RequestBody AppointmentClass appointmentClass) {

        return appointmentClassService.addAppointment(appointmentClass);
    }

    // Get All Appointments
    @GetMapping("/all")
    public List<AppointmentClass> getAllAppointments() {

        return appointmentClassService.getAllAppointments();
    }

    // Get Appointment By Id
    @GetMapping("/{id}")
    public AppointmentClass getAppointmentById(@PathVariable Long id) {

        return appointmentClassService.getAppointmentById(id);
    }

    // Update Appointment
    @PutMapping("/update/{id}")
    public AppointmentClass updateAppointment(@PathVariable Long id,
                                              @RequestBody AppointmentClass appointmentClass) {

        return appointmentClassService.updateAppointment(id, appointmentClass);
    }

    // Delete Appointment
    @DeleteMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {

        return appointmentClassService.deleteAppointment(id);
    }
    
    @GetMapping("/date/{date}")
    public List<AppointmentClass> getAppointmentsByDate(
            @PathVariable LocalDate date) {

        return appointmentClassService.getAppointmentsByDate(date);
    }
    
    @GetMapping("/employee/{id}")
    public List<AppointmentClass> getAppointmentsByEmployee(
            @PathVariable Long id) {

        return appointmentClassService.getAppointmentsByEmployee(id);
    }

}