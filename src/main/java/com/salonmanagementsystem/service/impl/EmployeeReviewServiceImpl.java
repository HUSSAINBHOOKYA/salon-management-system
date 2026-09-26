package com.salonmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.EmployeeReviewRequest;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.EmployeeReviewClass;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.EmployeeReviewRepository;
import com.salonmanagementsystem.service.EmployeeReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeReviewServiceImpl
        implements EmployeeReviewService {

    private final EmployeeReviewRepository employeeReviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeClassRepository;

    @Override
    @Transactional
    public EmployeeReviewClass addReview(
            EmployeeReviewRequest request,
            String customerEmail) {

        if (request.getEmployeeId() == null) {
            throw new RuntimeException("Employee ID is required");
        }

        if (request.getAppointmentId() == null) {
            throw new RuntimeException("Appointment ID is required");
        }

        validateRating(request.getRating());

        EmployeeClass employee = employeeClassRepository
                .findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        AppointmentClass appointment = appointmentRepository
                .findById(request.getAppointmentId())
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        if (appointment.getCustomerEmail() == null
                || !appointment.getCustomerEmail()
                .equalsIgnoreCase(customerEmail)) {

            throw new RuntimeException(
                    "You can review only your own appointment");
        }

        if (appointment.getEmployee() == null
                || appointment.getEmployee().getId() == null
                || !appointment.getEmployee().getId()
                .equals(employee.getId())) {

            throw new RuntimeException(
                    "This appointment does not belong to the selected employee");
        }

        if (appointment.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException(
                    "You can review the employee only after completing your appointment");
        }

        if (employeeReviewRepository.existsByAppointmentId(
                appointment.getId())) {

            throw new RuntimeException(
                    "You have already reviewed this appointment");
        }

        EmployeeReviewClass review = new EmployeeReviewClass();

        review.setEmployee(employee);
        review.setAppointment(appointment);
        review.setCustomerName(appointment.getCustomerName());
        review.setCustomerEmail(customerEmail);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        EmployeeReviewClass savedReview =
                employeeReviewRepository.save(review);

        updateEmployeeRating(employee);

        return savedReview;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeReviewClass> getEmployeeReviews(
            Long employeeId) {

        if (!employeeClassRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found");
        }

        return employeeReviewRepository
                .findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    private void updateEmployeeRating(EmployeeClass employee) {

        List<EmployeeReviewClass> reviews =
                employeeReviewRepository
                        .findByEmployeeIdOrderByCreatedAtDesc(
                                employee.getId());

        double averageRating = reviews.stream()
                .mapToInt(EmployeeReviewClass::getRating)
                .average()
                .orElse(0.0);

        employee.setRating(
                Math.round(averageRating * 100.0) / 100.0
        );

        employee.setReviewCount(reviews.size());

        employeeClassRepository.save(employee);
    }

    private void validateRating(Integer rating) {

        if (rating == null || rating < 1 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5");
        }
    }
}