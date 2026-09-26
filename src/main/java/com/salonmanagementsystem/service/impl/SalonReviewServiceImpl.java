package com.salonmanagementsystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.SalonReviewRequest;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.SalonReviewClass;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.SalonReviewRepository;
import com.salonmanagementsystem.service.SalonReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalonReviewServiceImpl implements SalonReviewService {

    private final SalonReviewRepository salonReviewRepository;
    private final AppointmentRepository appointmentRepository;
    private final SalonRepository salonRepository;

    @Override
    @Transactional
    public SalonReviewClass addReview(
            SalonReviewRequest request,
            String customerEmail) {

        if (request.getSalonId() == null) {
            throw new RuntimeException("Salon ID is required");
        }

        if (request.getAppointmentId() == null) {
            throw new RuntimeException("Appointment ID is required");
        }

        validateRating(request.getRating());

        Salon salon = salonRepository.findById(request.getSalonId())
                .orElseThrow(() ->
                        new RuntimeException("Salon not found"));

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

        if (appointment.getSalon() == null
                || appointment.getSalon().getId() == null
                || !appointment.getSalon().getId()
                .equals(salon.getId())) {

            throw new RuntimeException(
                    "This appointment does not belong to the selected salon");
        }

        if (appointment.getBookingStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException(
                    "You can review the salon only after completing your appointment");
        }

        if (salonReviewRepository.existsByAppointmentId(
                appointment.getId())) {

            throw new RuntimeException(
                    "You have already reviewed this appointment");
        }

        SalonReviewClass review = new SalonReviewClass();

        review.setSalon(salon);
        review.setAppointment(appointment);
        review.setCustomerName(appointment.getCustomerName());
        review.setCustomerEmail(customerEmail);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        SalonReviewClass savedReview =
                salonReviewRepository.save(review);

        updateSalonRating(salon);

        return savedReview;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalonReviewClass> getSalonReviews(Long salonId) {

        if (!salonRepository.existsById(salonId)) {
            throw new RuntimeException("Salon not found");
        }

        return salonReviewRepository
                .findBySalonIdOrderByCreatedAtDesc(salonId);
    }

    private void updateSalonRating(Salon salon) {

        List<SalonReviewClass> reviews =
                salonReviewRepository
                        .findBySalonIdOrderByCreatedAtDesc(salon.getId());

        double averageRating = reviews.stream()
                .mapToInt(SalonReviewClass::getRating)
                .average()
                .orElse(0.0);

        salon.setRating(
                Math.round(averageRating * 100.0) / 100.0
        );

        salon.setReviewCount(reviews.size());

        salonRepository.save(salon);
    }

    private void validateRating(Integer rating) {

        if (rating == null || rating < 1 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5");
        }
    }
}