package com.salonmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.TimeSlotClass;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlotClass, Long> {
        
    Optional<TimeSlotClass> findByEmployeeIdAndSlotDateAndSlotTime(
            Long employeeId,
            LocalDate slotDate,
            String slotTime);
    List<TimeSlotClass> findBySlotStatus(String slotStatus);
}