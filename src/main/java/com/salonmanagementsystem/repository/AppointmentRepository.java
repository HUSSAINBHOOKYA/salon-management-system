package com.salonmanagementsystem.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.AppointmentClass;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentClass, Long> {
	List<AppointmentClass> findByAppointmentDate(LocalDate appointmentDate);
	List<AppointmentClass> findByEmployeeId(Long employeeId);
}