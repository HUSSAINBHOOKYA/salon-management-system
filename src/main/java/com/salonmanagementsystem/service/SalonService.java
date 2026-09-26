package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.SalonRequest;
import com.salonmanagementsystem.dto.SalonResponse;

public interface SalonService {

    // =========================================
    // ADMIN - ADD SALON
    // =========================================

    SalonResponse addSalon(
            SalonRequest request);


    // =========================================
    // PARTNER - SUBMIT SALON APPLICATION
    // =========================================

    SalonResponse addPartnerSalon(
            SalonRequest request,
            String partnerEmail);


    // =========================================
    // PARTNER - APPLICATION STATUS
    // =========================================

    SalonResponse getPartnerApplicationStatus(
            String partnerEmail);


    // =========================================
    // ADMIN - GET ALL SALONS
    // =========================================

    List<SalonResponse> getAllSalons();


    // =========================================
    // ADMIN / CUSTOMER - GET SALON BY ID
    // =========================================

    SalonResponse getSalonById(
            Long id);


    // =========================================
    // ADMIN - UPDATE SALON
    // =========================================

    SalonResponse updateSalon(
            Long id,
            SalonRequest request);


    // =========================================
    // ADMIN - DELETE SALON
    // =========================================

    void deleteSalon(
            Long id);


    // =========================================
    // ADMIN - GET PENDING SALONS
    // =========================================

    List<SalonResponse> getPendingSalons();


    // =========================================
    // ADMIN - APPROVE SALON
    // =========================================

    SalonResponse approveSalon(
            Long id);


    // =========================================
    // ADMIN - REJECT SALON
    // =========================================

    SalonResponse rejectSalon(
            Long id);
}