package com.salonmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.enums.TimeSlotStatus;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
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
	private ServiceRepository serviceRepository;

	@Autowired
	private SalonRepository salonRepository;

	// =========================================
	// ADD TIME SLOT
	// =========================================

	public TimeSlotClass addTimeSlot(TimeSlotClass timeSlotClass) {

		// =====================================
		// FIND EMPLOYEE
		// =====================================

		EmployeeClass employee = employeeRepository.findById(timeSlotClass.getEmployee().getId())
				.orElseThrow(() -> new RuntimeException("Employee Not Found"));

		// =====================================
		// FIND SERVICE
		// =====================================

		SalonService service = serviceRepository.findById(timeSlotClass.getSalonService().getId())
				.orElseThrow(() -> new RuntimeException("Service Not Found"));

		// =====================================
		// FIND SALON
		// =====================================

		Salon salon = salonRepository.findById(timeSlotClass.getSalon().getId())
				.orElseThrow(() -> new RuntimeException("Salon Not Found"));

		// =====================================
		// CHECK DUPLICATE SLOT
		// =====================================

		Optional<TimeSlotClass> existingSlot = timeSlotRepository.findByEmployeeIdAndSlotDateAndSlotTime(
				employee.getId(), timeSlotClass.getSlotDate(), timeSlotClass.getSlotTime());

		if (existingSlot.isPresent()) {

			throw new RuntimeException("This Time Slot Already Exists.");
		}

		// =====================================
		// CONNECT EMPLOYEE
		// =====================================

		timeSlotClass.setEmployee(employee);

		// =====================================
		// CONNECT SERVICE
		// =====================================

		timeSlotClass.setSalonService(service);

		// =====================================
		// CONNECT SALON
		// =====================================

		timeSlotClass.setSalon(salon);

		// =====================================
		// SAVE
		// =====================================

		return timeSlotRepository.save(timeSlotClass);
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

	public TimeSlotClass getTimeSlotById(Long id) {

		return timeSlotRepository.findById(id).orElseThrow(() -> new RuntimeException("Time Slot Not Found"));
	}

	// =========================================
	// UPDATE TIME SLOT
	// =========================================

	public TimeSlotClass updateTimeSlot(Long id, TimeSlotClass timeSlotClass) {

		// =====================================
		// FIND EXISTING TIME SLOT
		// =====================================

		TimeSlotClass existingTimeSlot = timeSlotRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Time Slot Not Found"));

		// =====================================
		// FIND EMPLOYEE
		// =====================================

		EmployeeClass employee = employeeRepository.findById(timeSlotClass.getEmployee().getId())
				.orElseThrow(() -> new RuntimeException("Employee Not Found"));

		// =====================================
		// FIND SERVICE
		// =====================================

		SalonService service = serviceRepository.findById(timeSlotClass.getSalonService().getId())
				.orElseThrow(() -> new RuntimeException("Service Not Found"));

		// =====================================
		// FIND SALON
		// =====================================

		Salon salon = salonRepository.findById(timeSlotClass.getSalon().getId())
				.orElseThrow(() -> new RuntimeException("Salon Not Found"));

		// =====================================
		// CHECK DUPLICATE SLOT
		// =====================================

		Optional<TimeSlotClass> duplicateSlot = timeSlotRepository.findByEmployeeIdAndSlotDateAndSlotTime(
				employee.getId(), timeSlotClass.getSlotDate(), timeSlotClass.getSlotTime());

		if (duplicateSlot.isPresent() && !duplicateSlot.get().getId().equals(id)) {

			throw new RuntimeException("This Time Slot Already Exists.");
		}

		// =====================================
		// UPDATE DATE
		// =====================================

		existingTimeSlot.setSlotDate(timeSlotClass.getSlotDate());

		// =====================================
		// UPDATE TIME
		// =====================================

		existingTimeSlot.setSlotTime(timeSlotClass.getSlotTime());

		// =====================================
		// UPDATE STATUS
		// =====================================

		existingTimeSlot.setSlotStatus(timeSlotClass.getSlotStatus());

		// =====================================
		// UPDATE EMPLOYEE
		// =====================================

		existingTimeSlot.setEmployee(employee);

		// =====================================
		// UPDATE SERVICE
		// =====================================

		existingTimeSlot.setSalonService(service);

		// =====================================
		// UPDATE SALON
		// =====================================

		existingTimeSlot.setSalon(salon);

		// =====================================
		// SAVE
		// =====================================

		return timeSlotRepository.save(existingTimeSlot);
	}

	// =========================================
	// DELETE TIME SLOT
	// =========================================

	public String deleteTimeSlot(Long id) {

		TimeSlotClass timeSlot = timeSlotRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Time Slot Not Found"));

		timeSlotRepository.delete(timeSlot);

		return "Time Slot Deleted Successfully";
	}

	// =========================================
	// AVAILABLE TIME SLOTS
	// =========================================

	public List<TimeSlotClass> getAvailableSlots() {

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

		return timeSlotRepository.findBySlotStatus(TimeSlotStatus.AVAILABLE, pageable).getContent();
	}

	// =========================================
	// SEARCH TIME SLOTS
	// =========================================

	public List<TimeSlotClass> searchTimeSlots(String slotTime) {

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

		return timeSlotRepository.findBySlotTimeContainingIgnoreCase(slotTime, pageable).getContent();
	}

	// =========================================
	// PAGINATION
	// =========================================

	public PaginationResponse<TimeSlotClass> getTimeSlotPagination(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<TimeSlotClass> slotPage = timeSlotRepository.findAll(pageable);

		return new PaginationResponse<>(

				slotPage.getContent(),

				slotPage.getNumber(),

				slotPage.getTotalPages(),

				slotPage.getTotalElements(),

				slotPage.getSize());
	}

	// =========================================
	// FILTER BY STATUS
	// =========================================

	public List<TimeSlotClass> getSlotsByStatus(TimeSlotStatus status) {

		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

		return timeSlotRepository.findBySlotStatus(status, pageable).getContent();
	}
	// =========================================
	// GET TIME SLOTS BY SALON
	// =========================================

	public List<TimeSlotClass> getTimeSlotsBySalon(Long salonId) {

		return timeSlotRepository.findBySalonId(salonId);
	}

//=========================================
//GET AVAILABLE SLOTS
//BY SALON + EMPLOYEE + DATE
//=========================================

	public List<TimeSlotClass> getAvailableSlotsBySalonEmployeeDate(Long salonId, Long employeeId, LocalDate date) {

		return timeSlotRepository
				.findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(salonId, employeeId, date,
				TimeSlotStatus.AVAILABLE);
	}
}