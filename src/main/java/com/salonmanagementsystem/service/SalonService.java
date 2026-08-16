package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.SalonRequest;
import com.salonmanagementsystem.dto.SalonResponse;

public interface SalonService {

    SalonResponse addSalon(
            SalonRequest request);

    List<SalonResponse> getAllSalons();

    SalonResponse getSalonById(
            Long id);

    SalonResponse updateSalon(
            Long id,
            SalonRequest request);

    void deleteSalon(
            Long id);

}