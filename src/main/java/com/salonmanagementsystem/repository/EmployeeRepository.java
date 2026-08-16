package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.enums.EmployeeStatus;

public interface EmployeeRepository
        extends JpaRepository<EmployeeClass, Long> {


    // =========================================
    // SEARCH EMPLOYEE
    // =========================================

    List<EmployeeClass>
    findByEmployeeNameContainingIgnoreCase(
            String employeeName
    );


    // =========================================
    // PAGINATION
    // =========================================

    Page<EmployeeClass>
    findAll(Pageable pageable);


    // =========================================
    // STATUS FILTER
    // =========================================

    List<EmployeeClass>
    findByStatus(
            EmployeeStatus status
    );


    // =========================================
    // SALON FILTER
    // =========================================

    List<EmployeeClass>
    findBySalonId(
            Long salonId
    );


    // =========================================
    // SALON + STATUS
    // =========================================

    List<EmployeeClass>
    findBySalonIdAndStatus(
            Long salonId,
            EmployeeStatus status
    );

}