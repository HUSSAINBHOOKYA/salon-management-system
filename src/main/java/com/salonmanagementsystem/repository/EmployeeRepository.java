package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.enums.EmployeeStatus;
import com.salonmanagementsystem.enums.Gender;

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


    // =========================================
    // SERVICE → EMPLOYEES
    // =========================================

    List<EmployeeClass>
    findByServicesId(
            Long serviceId
    );


    // =========================================
    // TOP RATED EMPLOYEES BY SERVICE GENDER
    // =========================================

    List<EmployeeClass>
    findDistinctByServices_GenderOrderByRatingDesc(
            Gender gender
    );


    // =========================================
    // TOP RATED ACTIVE EMPLOYEES BY
    // SERVICE GENDER
    // =========================================

    List<EmployeeClass>
    findDistinctByServices_GenderAndStatusOrderByRatingDesc(
            Gender gender,
            EmployeeStatus status
    );

}