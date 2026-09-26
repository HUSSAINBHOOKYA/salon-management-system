package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.EmployeeReviewClass;

@Repository
public interface EmployeeReviewRepository
        extends JpaRepository<EmployeeReviewClass, Long> {

    List<EmployeeReviewClass> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    boolean existsByAppointmentId(Long appointmentId);

    long countByEmployeeId(Long employeeId);
}