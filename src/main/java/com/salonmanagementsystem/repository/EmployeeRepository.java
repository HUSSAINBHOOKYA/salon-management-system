package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.EmployeeClass;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeClass, Long> {
	List<EmployeeClass> findByEmployeeNameContainingIgnoreCase(String employeeName);
}