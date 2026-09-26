package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.SalonReviewClass;

@Repository
public interface SalonReviewRepository
        extends JpaRepository<SalonReviewClass, Long> {

    List<SalonReviewClass> findBySalonIdOrderByCreatedAtDesc(Long salonId);

    boolean existsByAppointmentId(Long appointmentId);

    long countBySalonId(Long salonId);
}