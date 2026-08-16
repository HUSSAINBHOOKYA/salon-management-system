package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.enums.ServiceStatus;

public interface ServiceRepository
        extends JpaRepository<SalonService, Long> {


    // =========================================
    // SALON FILTER
    // =========================================

    List<SalonService>
    findBySalonId(Long salonId);


    // =========================================
    // SALON + STATUS
    // =========================================

    List<SalonService>
    findBySalonIdAndStatus(
            Long salonId,
            ServiceStatus status
    );


    // =========================================
    // EMPLOYEE FILTER
    // =========================================

    List<SalonService>
    findByEmployeesId(Long employeeId);

}