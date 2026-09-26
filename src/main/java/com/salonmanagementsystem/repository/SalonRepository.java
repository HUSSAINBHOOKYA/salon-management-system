package com.salonmanagementsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.enums.SalonStatus;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

    // =========================================
    // FIND SALON BY SALON EMAIL
    // =========================================

    Optional<Salon> findByEmail(String email);


    // =========================================
    // FIND SALON BY PARTNER EMAIL
    // =========================================

    Optional<Salon> findByPartnerEmail(String partnerEmail);


    // =========================================
    // FIND SALONS BY STATUS
    // =========================================

    List<Salon> findByStatus(SalonStatus status);

}