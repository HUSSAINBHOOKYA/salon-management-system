package com.salonmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.enums.TimeSlotStatus;

public interface TimeSlotRepository
        extends JpaRepository<TimeSlotClass, Long> {

    // =========================================
    // FIND DUPLICATE SLOT
    // =========================================

    Optional<TimeSlotClass> findByEmployeeIdAndSlotDateAndSlotTime(
            Long employeeId,
            LocalDate slotDate,
            String slotTime
    );

    // =========================================
    // SEARCH BY SLOT TIME
    // =========================================

    Page<TimeSlotClass> findBySlotTimeContainingIgnoreCase(
            String slotTime,
            Pageable pageable
    );

    // =========================================
    // FILTER BY STATUS
    // =========================================

    Page<TimeSlotClass> findBySlotStatus(
            TimeSlotStatus slotStatus,
            Pageable pageable
    );

    // =========================================
    // GET TIME SLOTS BY SALON
    // =========================================

    List<TimeSlotClass> findBySalonId(
            Long salonId
    );

    // =========================================
    // AVAILABLE SLOTS
    // BY SALON + EMPLOYEE + DATE
    // =========================================

    List<TimeSlotClass>
    findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(
            Long salonId,
            Long employeeId,
            LocalDate slotDate,
            TimeSlotStatus slotStatus
    );
}