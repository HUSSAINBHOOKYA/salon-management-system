package com.salonmanagementsystem.controller;

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

import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.service.TimeSlotClassService;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotClassController {

    @Autowired
    private TimeSlotClassService timeSlotClassService;

    // Add Time Slot
    @PostMapping("/add")
    public TimeSlotClass addTimeSlot(@RequestBody TimeSlotClass timeSlotClass) {

        return timeSlotClassService.addTimeSlot(timeSlotClass);
    }

    // Get All Time Slots
    @GetMapping("/all")
    public List<TimeSlotClass> getAllTimeSlots() {

        return timeSlotClassService.getAllTimeSlots();
    }

    // Get Time Slot By Id
    @GetMapping("/{id}")
    public TimeSlotClass getTimeSlotById(@PathVariable Long id) {

        return timeSlotClassService.getTimeSlotById(id);
    }

    // Update Time Slot
    @PutMapping("/update/{id}")
    public TimeSlotClass updateTimeSlot(@PathVariable Long id,
                                        @RequestBody TimeSlotClass timeSlotClass) {

        return timeSlotClassService.updateTimeSlot(id, timeSlotClass);
    }

    // Delete Time Slot
    @DeleteMapping("/delete/{id}")
    public String deleteTimeSlot(@PathVariable Long id) {

        return timeSlotClassService.deleteTimeSlot(id);
    }
    
    @GetMapping("/available")
    public List<TimeSlotClass> getAvailableSlots() {
        return timeSlotClassService.getAvailableSlots();
    }
}