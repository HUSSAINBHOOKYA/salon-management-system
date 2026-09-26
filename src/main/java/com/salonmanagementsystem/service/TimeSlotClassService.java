package com.salonmanagementsystem.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.dto.TimeSlotBatchRequest;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.enums.TimeSlotStatus;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.TimeSlotRepository;

@Service
public class TimeSlotClassService {

    // =========================================
    // REPOSITORIES
    // =========================================

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalonRepository salonRepository;

    // =========================================
    // ADD SINGLE TIME SLOT
    // =========================================

    public TimeSlotClass addTimeSlot(
            TimeSlotClass timeSlotClass) {

        if (timeSlotClass == null) {
            throw new RuntimeException(
                    "Time Slot data is required"
            );
        }

        if (timeSlotClass.getSalon() == null ||
                timeSlotClass.getSalon().getId() == null) {

            throw new RuntimeException(
                    "Salon is required"
            );
        }

        if (timeSlotClass.getEmployee() == null ||
                timeSlotClass.getEmployee().getId() == null) {

            throw new RuntimeException(
                    "Employee is required"
            );
        }

        if (timeSlotClass.getSlotDate() == null) {

            throw new RuntimeException(
                    "Slot Date is required"
            );
        }

        if (timeSlotClass.getSlotTime() == null ||
                timeSlotClass.getSlotTime().isBlank()) {

            throw new RuntimeException(
                    "Slot Time is required"
            );
        }

        Long salonId =
                timeSlotClass
                        .getSalon()
                        .getId();

        Long employeeId =
                timeSlotClass
                        .getEmployee()
                        .getId();

        Salon salon =
                salonRepository
                        .findById(salonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"
                                )
                        );

        EmployeeClass employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"
                                )
                        );

        validateEmployeeBelongsToSalon(
                employee,
                salon
        );

        Optional<TimeSlotClass> existingSlot =
                timeSlotRepository
                        .findByEmployeeIdAndSlotDateAndSlotTime(
                                employeeId,
                                timeSlotClass.getSlotDate(),
                                timeSlotClass.getSlotTime()
                        );

        if (existingSlot.isPresent()) {

            throw new RuntimeException(
                    "This Time Slot Already Exists."
            );
        }

        if (timeSlotClass.getSlotStatus() == null) {

            timeSlotClass.setSlotStatus(
                    TimeSlotStatus.AVAILABLE
            );
        }

        timeSlotClass.setSalon(salon);

        timeSlotClass.setEmployee(employee);

        return timeSlotRepository.save(
                timeSlotClass
        );
    }

    // =========================================
    // ADD MULTIPLE TIME SLOTS
    // =========================================

    public List<TimeSlotClass> addMultipleTimeSlots(
            TimeSlotBatchRequest request) {

        if (request == null) {

            throw new RuntimeException(
                    "Time Slot data is required"
            );
        }

        if (request.getSalonId() == null) {

            throw new RuntimeException(
                    "Salon is required"
            );
        }

        if (request.getEmployeeId() == null) {

            throw new RuntimeException(
                    "Employee is required"
            );
        }

        if (request.getSlotDate() == null) {

            throw new RuntimeException(
                    "Slot Date is required"
            );
        }

        if (request.getSlotTimes() == null ||
                request.getSlotTimes().isEmpty()) {

            throw new RuntimeException(
                    "At least one Slot Time is required"
            );
        }

        Salon salon =
                salonRepository
                        .findById(request.getSalonId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"
                                )
                        );

        EmployeeClass employee =
                employeeRepository
                        .findById(request.getEmployeeId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"
                                )
                        );

        validateEmployeeBelongsToSalon(
                employee,
                salon
        );

        // =====================================
        // TIME FORMATTER
        // =====================================

        DateTimeFormatter formatter =
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("hh:mm a")
                        .toFormatter(Locale.ENGLISH)
                        .withResolverStyle(
                                ResolverStyle.SMART
                        );

        // =====================================
        // CREATED SLOTS
        // =====================================

        List<TimeSlotClass> createdSlots =
                new ArrayList<>();

        // =====================================
        // PROCESS EACH TIME
        // =====================================

        for (String slotTime :
                request.getSlotTimes()) {

            // =================================
            // CHECK EMPTY TIME
            // =================================

            if (slotTime == null ||
                    slotTime.isBlank()) {

                throw new RuntimeException(
                        "Slot Time cannot be empty"
                );
            }

            // =================================
            // NORMALIZE TIME
            // =================================

            String formattedSlotTime =
                    slotTime
                            .trim()
                            .replaceAll("\\s+", " ")
                            .toUpperCase(Locale.ENGLISH);

            // =================================
            // VALIDATE TIME FORMAT
            // =================================

            try {

                LocalTime.parse(
                        formattedSlotTime,
                        formatter
                );

            } catch (Exception e) {

                throw new RuntimeException(
                        "Invalid time format: "
                        + formattedSlotTime
                        + ". Use hh:mm AM/PM"
                );
            }

            // =================================
            // CHECK DUPLICATE
            // =================================

            Optional<TimeSlotClass> existingSlot =
                    timeSlotRepository
                            .findByEmployeeIdAndSlotDateAndSlotTime(
                                    employee.getId(),
                                    request.getSlotDate(),
                                    formattedSlotTime
                            );

            // =================================
            // CREATE IF NOT EXISTS
            // =================================

            if (existingSlot.isEmpty()) {

                TimeSlotClass slot =
                        new TimeSlotClass();

                // =============================
                // DATE
                // =============================

                slot.setSlotDate(
                        request.getSlotDate()
                );

                // =============================
                // TIME
                // =============================

                slot.setSlotTime(
                        formattedSlotTime
                );

                // =============================
                // STATUS
                // =============================

                slot.setSlotStatus(
                        TimeSlotStatus.AVAILABLE
                );

                // =============================
                // SALON
                // =============================

                slot.setSalon(
                        salon
                );

                // =============================
                // EMPLOYEE
                // =============================

                slot.setEmployee(
                        employee
                );

                // =============================
                // SAVE
                // =============================

                TimeSlotClass savedSlot =
                        timeSlotRepository.save(
                                slot
                        );

                createdSlots.add(
                        savedSlot
                );
            }
        }

        // =====================================
        // CHECK RESULT
        // =====================================

        if (createdSlots.isEmpty()) {

            throw new RuntimeException(
                    "All requested time slots "
                    + "already exist."
            );
        }

        // =====================================
        // RETURN
        // =====================================

        return createdSlots;
    }

    // =========================================
    // GET ALL TIME SLOTS
    // =========================================

    public List<TimeSlotClass> getAllTimeSlots() {

        return timeSlotRepository.findAll();
    }

    // =========================================
    // GET TIME SLOT BY ID
    // =========================================

    public TimeSlotClass getTimeSlotById(
            Long id) {

        return timeSlotRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Time Slot Not Found"
                        )
                );
    }

    // =========================================
    // UPDATE TIME SLOT
    // =========================================

    public TimeSlotClass updateTimeSlot(
            Long id,
            TimeSlotClass timeSlotClass) {

        TimeSlotClass existingTimeSlot =
                timeSlotRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Time Slot Not Found"
                                )
                        );

        if (timeSlotClass == null) {

            throw new RuntimeException(
                    "Time Slot data is required"
            );
        }

        if (timeSlotClass.getSalon() == null ||
                timeSlotClass.getSalon().getId() == null) {

            throw new RuntimeException(
                    "Salon is required"
            );
        }

        if (timeSlotClass.getEmployee() == null ||
                timeSlotClass.getEmployee().getId() == null) {

            throw new RuntimeException(
                    "Employee is required"
            );
        }

        if (timeSlotClass.getSlotDate() == null) {

            throw new RuntimeException(
                    "Slot Date is required"
            );
        }

        if (timeSlotClass.getSlotTime() == null ||
                timeSlotClass.getSlotTime().isBlank()) {

            throw new RuntimeException(
                    "Slot Time is required"
            );
        }

        Salon salon =
                salonRepository
                        .findById(
                                timeSlotClass
                                        .getSalon()
                                        .getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"
                                )
                        );

        EmployeeClass employee =
                employeeRepository
                        .findById(
                                timeSlotClass
                                        .getEmployee()
                                        .getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"
                                )
                        );

        validateEmployeeBelongsToSalon(
                employee,
                salon
        );

        Optional<TimeSlotClass> duplicateSlot =
                timeSlotRepository
                        .findByEmployeeIdAndSlotDateAndSlotTime(
                                employee.getId(),
                                timeSlotClass.getSlotDate(),
                                timeSlotClass.getSlotTime()
                        );

        if (duplicateSlot.isPresent() &&
                !duplicateSlot
                        .get()
                        .getId()
                        .equals(id)) {

            throw new RuntimeException(
                    "This Time Slot Already Exists."
            );
        }

        existingTimeSlot.setSlotDate(
                timeSlotClass.getSlotDate()
        );

        existingTimeSlot.setSlotTime(
                timeSlotClass.getSlotTime()
        );

        if (timeSlotClass.getSlotStatus() != null) {

            existingTimeSlot.setSlotStatus(
                    timeSlotClass.getSlotStatus()
            );
        }

        existingTimeSlot.setSalon(
                salon
        );

        existingTimeSlot.setEmployee(
                employee
        );

        return timeSlotRepository.save(
                existingTimeSlot
        );
    }

    // =========================================
    // DELETE TIME SLOT
    // =========================================

    public String deleteTimeSlot(
            Long id) {

        TimeSlotClass timeSlot =
                timeSlotRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Time Slot Not Found"
                                )
                        );

        timeSlotRepository.delete(
                timeSlot
        );

        return "Time Slot Deleted Successfully";
    }

    // =========================================
    // GET ALL AVAILABLE TIME SLOTS
    // =========================================

    public List<TimeSlotClass> getAvailableSlots() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE
                );

        return timeSlotRepository
                .findBySlotStatus(
                        TimeSlotStatus.AVAILABLE,
                        pageable
                )
                .getContent();
    }

    // =========================================
    // SEARCH TIME SLOTS
    // =========================================

    public List<TimeSlotClass> searchTimeSlots(
            String slotTime) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE
                );

        return timeSlotRepository
                .findBySlotTimeContainingIgnoreCase(
                        slotTime,
                        pageable
                )
                .getContent();
    }

    // =========================================
    // PAGINATION
    // =========================================

    public PaginationResponse<TimeSlotClass>
    getTimeSlotPagination(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        Page<TimeSlotClass> slotPage =
                timeSlotRepository
                        .findAll(pageable);

        return new PaginationResponse<>(
                slotPage.getContent(),
                slotPage.getNumber(),
                slotPage.getTotalPages(),
                slotPage.getTotalElements(),
                slotPage.getSize()
        );
    }

    // =========================================
    // FILTER BY STATUS
    // =========================================

    public List<TimeSlotClass> getSlotsByStatus(
            TimeSlotStatus status) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE
                );

        return timeSlotRepository
                .findBySlotStatus(
                        status,
                        pageable
                )
                .getContent();
    }

    // =========================================
    // GET TIME SLOTS BY SALON
    // =========================================

    public List<TimeSlotClass> getTimeSlotsBySalon(
            Long salonId) {

        salonRepository
                .findById(salonId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salon Not Found"
                        )
                );

        return timeSlotRepository
                .findBySalonId(salonId);
    }

    // =========================================
    // GET AVAILABLE SLOTS
    // BY SALON + EMPLOYEE + DATE
    // =========================================

    public List<TimeSlotClass>
    getAvailableSlotsBySalonEmployeeDate(
            Long salonId,
            Long employeeId,
            LocalDate date) {

        Salon salon =
                salonRepository
                        .findById(salonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"
                                )
                        );

        EmployeeClass employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"
                                )
                        );

        if (date == null) {

            throw new RuntimeException(
                    "Slot Date is required"
            );
        }

        validateEmployeeBelongsToSalon(
                employee,
                salon
        );

        return timeSlotRepository
                .findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(
                        salonId,
                        employeeId,
                        date,
                        TimeSlotStatus.AVAILABLE
                );
    }

    // =========================================
    // VALIDATE EMPLOYEE → SALON
    // =========================================

    private void validateEmployeeBelongsToSalon(
            EmployeeClass employee,
            Salon salon) {

        if (employee.getSalon() == null) {

            throw new RuntimeException(
                    "Employee is not assigned to a salon"
            );
        }

        if (!employee
                .getSalon()
                .getId()
                .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Employee does not belong "
                    + "to the selected Salon"
            );
        }
    }
}