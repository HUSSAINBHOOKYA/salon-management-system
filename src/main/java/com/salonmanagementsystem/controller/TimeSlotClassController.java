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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.dto.TimeSlotBatchRequest;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.enums.TimeSlotStatus;
import com.salonmanagementsystem.service.TimeSlotClassService;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotClassController {

    @Autowired
    private TimeSlotClassService timeSlotClassService;
    
 // =========================================
 // ADD MULTIPLE TIME SLOTS
 // =========================================

 @PostMapping("/add-multiple")
 public List<TimeSlotClass> addMultipleTimeSlots(
         @RequestBody TimeSlotBatchRequest request) {

     return timeSlotClassService
             .addMultipleTimeSlots(request);
 }

    // =========================================
    // ADD TIME SLOT
    // =========================================

    @PostMapping("/add")
    public TimeSlotClass addTimeSlot(
            @RequestBody TimeSlotClass timeSlotClass) {

        return timeSlotClassService.addTimeSlot(timeSlotClass);
    }

    // =========================================
    // GET ALL TIME SLOTS
    // =========================================

    @GetMapping("/all")
    public List<TimeSlotClass> getAllTimeSlots() {

        return timeSlotClassService.getAllTimeSlots();
    }

    // =========================================
    // GET TIME SLOT BY ID
    // =========================================

    @GetMapping("/{id}")
    public TimeSlotClass getTimeSlotById(
            @PathVariable Long id) {

        return timeSlotClassService.getTimeSlotById(id);
    }

    // =========================================
    // UPDATE TIME SLOT
    // =========================================

    @PutMapping("/update/{id}")
    public TimeSlotClass updateTimeSlot(
            @PathVariable Long id,
            @RequestBody TimeSlotClass timeSlotClass) {

        return timeSlotClassService.updateTimeSlot(
                id,
                timeSlotClass
        );
    }

    // =========================================
    // DELETE TIME SLOT
    // =========================================

    @DeleteMapping("/delete/{id}")
    public String deleteTimeSlot(
            @PathVariable Long id) {

        return timeSlotClassService.deleteTimeSlot(id);
    }

    // =========================================
    // ALL AVAILABLE SLOTS
    // =========================================

    @GetMapping("/available")
    public List<TimeSlotClass> getAvailableSlots() {

        return timeSlotClassService.getAvailableSlots();
    }

    // =========================================
    // SEARCH TIME SLOTS
    // =========================================

    @GetMapping("/search")
    public List<TimeSlotClass> searchTimeSlots(
            @RequestParam String time) {

        return timeSlotClassService.searchTimeSlots(time);
    }

    // =========================================
    // PAGINATION
    // =========================================

    @GetMapping("/pagination")
    public PaginationResponse<TimeSlotClass> getTimeSlotPagination(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return timeSlotClassService.getTimeSlotPagination(
                page,
                size
        );
    }

    // =========================================
    // FILTER BY STATUS
    // =========================================

    @GetMapping("/status/{status}")
    public List<TimeSlotClass> getSlotsByStatus(
            @PathVariable TimeSlotStatus status) {

        return timeSlotClassService.getSlotsByStatus(status);
    }

    // =========================================
    // GET SLOTS BY SALON
    // =========================================

    @GetMapping("/salon/{salonId}")
    public List<TimeSlotClass> getTimeSlotsBySalon(
            @PathVariable Long salonId) {

        return timeSlotClassService
                .getTimeSlotsBySalon(salonId);
    }

    // =========================================
    // AVAILABLE SLOTS
    // BY SALON + EMPLOYEE + DATE
    // =========================================

    @GetMapping("/available/salon/{salonId}/employee/{employeeId}/date/{date}")
    public List<TimeSlotClass> getAvailableSlotsBySalonEmployeeDate(

            @PathVariable Long salonId,

            @PathVariable Long employeeId,

            @PathVariable LocalDate date) {

        return timeSlotClassService
                .getAvailableSlotsBySalonEmployeeDate(
                        salonId,
                        employeeId,
                        date
                );
    }
}