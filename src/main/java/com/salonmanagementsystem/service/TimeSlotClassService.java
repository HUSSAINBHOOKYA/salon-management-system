package com.salonmanagementsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.ServiceClass;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
import com.salonmanagementsystem.repository.TimeSlotRepository;

@Service
public class TimeSlotClassService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    
    
    @Autowired
    private ServiceRepository serviceRepository;

    // Add Time Slot
    public TimeSlotClass addTimeSlot(TimeSlotClass timeSlotClass) {

        EmployeeClass employee = employeeRepository
                .findById(timeSlotClass.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));


        ServiceClass service = serviceRepository
                .findById(timeSlotClass.getService().getId())
                .orElseThrow(() -> new RuntimeException("Service Not Found"));


        Optional<TimeSlotClass> existingSlot =
                timeSlotRepository.findByEmployeeIdAndSlotDateAndSlotTime(
                        employee.getId(),
                        timeSlotClass.getSlotDate(),
                        timeSlotClass.getSlotTime()
                );


        if(existingSlot.isPresent()) {
            throw new RuntimeException("This time slot already exists.");
        }


        timeSlotClass.setEmployee(employee);
        timeSlotClass.setService(service);


        return timeSlotRepository.save(timeSlotClass);
    }

    // Get All Time Slots
    public List<TimeSlotClass> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    // Get Time Slot By Id
    public TimeSlotClass getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id).orElse(null);
    }

    // Update Time Slot
    public TimeSlotClass updateTimeSlot(Long id, TimeSlotClass timeSlotClass) {

        TimeSlotClass existingTimeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time Slot Not Found"));

        EmployeeClass employee = employeeRepository
                .findById(timeSlotClass.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        Optional<TimeSlotClass> duplicateSlot = timeSlotRepository
                .findByEmployeeIdAndSlotDateAndSlotTime(
                        employee.getId(),
                        timeSlotClass.getSlotDate(),
                        timeSlotClass.getSlotTime());

        if (duplicateSlot.isPresent() && !duplicateSlot.get().getId().equals(id)) {
            throw new RuntimeException("This time slot already exists.");
        }

        existingTimeSlot.setSlotDate(timeSlotClass.getSlotDate());
        existingTimeSlot.setSlotTime(timeSlotClass.getSlotTime());
        existingTimeSlot.setSlotStatus(timeSlotClass.getSlotStatus());
        existingTimeSlot.setEmployee(employee);

        return timeSlotRepository.save(existingTimeSlot);
    }

    // Delete Time Slot
    public String deleteTimeSlot(Long id) {

        if (timeSlotRepository.existsById(id)) {
            timeSlotRepository.deleteById(id);
            return "Time Slot Deleted Successfully";
        }

        return "Time Slot Not Found";
    }
    
    public List<TimeSlotClass> getAvailableSlots() {
        return timeSlotRepository.findBySlotStatus("AVAILABLE");
    }
}





















//package UseProjectAuthenticationandAuthorization.simple.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import UseProjectAuthenticationandAuthorization.simple.entity.EmployeeClass;
//import UseProjectAuthenticationandAuthorization.simple.entity.TimeSlotClass;
//import UseProjectAuthenticationandAuthorization.simple.repository.EmployeeRepository;
//import UseProjectAuthenticationandAuthorization.simple.repository.TimeSlotRepository;
//
//@Service
//public class TimeSlotClassService {
//
//    @Autowired
//    private TimeSlotRepository timeSlotRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    // Add Time Slot
//    
//    public TimeSlotClass addTimeSlot(TimeSlotClass timeSlotClass) {
//
//        EmployeeClass employee = employeeRepository
//                .findById(timeSlotClass.getEmployee().getId())
//                .orElseThrow(() -> new RuntimeException("Employee Not Found"));
//
//        Optional<TimeSlotClass> existingSlot = timeSlotRepository
//                .findByEmployeeIdAndSlotDateAndSlotTime(
//                        employee.getId(),
//                        timeSlotClass.getSlotDate(),
//                        timeSlotClass.getSlotTime());
//
//        if (existingSlot.isPresent()) {
//            throw new RuntimeException("This time slot already exists.");
//        }
//
//        timeSlotClass.setEmployee(employee);
//
//        return timeSlotRepository.save(timeSlotClass);
//    }
//    
//    
////    public TimeSlotClass addTimeSlot(TimeSlotClass timeSlotClass) {
////
////        EmployeeClass employee = employeeRepository
////                .findById(timeSlotClass.getEmployee().getId())
////                .orElseThrow(() -> new RuntimeException("Employee Not Found"));
////
////        timeSlotClass.setEmployee(employee);
////
////        return timeSlotRepository.save(timeSlotClass);
////    }
//
//    // Get All Time Slots
//    public List<TimeSlotClass> getAllTimeSlots() {
//        return timeSlotRepository.findAll();
//    }
//
//    // Get Time Slot By Id
//    public TimeSlotClass getTimeSlotById(Long id) {
//        return timeSlotRepository.findById(id).orElse(null);
//    }
//
//    // Update Time Slot
//    public TimeSlotClass updateTimeSlot(Long id, TimeSlotClass timeSlotClass) {
//
//        TimeSlotClass existingTimeSlot = timeSlotRepository.findById(id).orElse(null);
//
//        if (existingTimeSlot != null) {
//
//            EmployeeClass employee = employeeRepository
//                    .findById(timeSlotClass.getEmployee().getId())
//                    .orElseThrow(() -> new RuntimeException("Employee Not Found"));
//
//            existingTimeSlot.setSlotDate(timeSlotClass.getSlotDate());
//            existingTimeSlot.setSlotTime(timeSlotClass.getSlotTime());
//            existingTimeSlot.setSlotStatus(timeSlotClass.getSlotStatus());
//            existingTimeSlot.setEmployee(employee);
//
//            return timeSlotRepository.save(existingTimeSlot);
//        }
//
//        return null;
//    }
//
//    // Delete Time Slot
//    public String deleteTimeSlot(Long id) {
//
//        if (timeSlotRepository.existsById(id)) {
//
//            timeSlotRepository.deleteById(id);
//
//            return "Time Slot Deleted Successfully";
//        }
//
//        return "Time Slot Not Found";
//    }
//}