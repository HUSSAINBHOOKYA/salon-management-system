package com.salonmanagementsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.SalonRequest;
import com.salonmanagementsystem.dto.SalonResponse;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.util.FileUploadUtil;

@Service
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    private final FileUploadUtil fileUploadUtil;


    public SalonServiceImpl(
            SalonRepository salonRepository,
            FileUploadUtil fileUploadUtil) {

        this.salonRepository = salonRepository;

        this.fileUploadUtil = fileUploadUtil;
    }


    // =========================================
    // ADD SALON
    // =========================================

    @Override
    public SalonResponse addSalon(
            SalonRequest request) {

        try {

            Salon salon = new Salon();


            // =================================
            // SALON DETAILS
            // =================================

            salon.setSalonName(
                    request.getSalonName());

            salon.setOwnerName(
                    request.getOwnerName());

            salon.setEmail(
                    request.getEmail());

            salon.setPhoneNumber(
                    request.getPhoneNumber());

            salon.setAddress(
                    request.getAddress());

            salon.setCity(
                    request.getCity());

            salon.setState(
                    request.getState());

            salon.setPincode(
                    request.getPincode());

            salon.setOpeningTime(
                    request.getOpeningTime());

            salon.setClosingTime(
                    request.getClosingTime());

            salon.setDescription(
                    request.getDescription());


            // =================================
            // RATING
            // =================================

            if (request.getRating() != null) {

                salon.setRating(
                        request.getRating());

            } else {

                salon.setRating(0.0);

            }


            // =================================
            // REVIEW COUNT
            // =================================

            if (request.getReviewCount() != null) {

                salon.setReviewCount(
                        request.getReviewCount());

            } else {

                salon.setReviewCount(0);

            }


            // =================================
            // STATUS
            // =================================

            salon.setStatus(
                    request.getStatus());


            // =================================
            // UPLOAD LOGO
            // =================================

            String logoUrl =
                    fileUploadUtil.uploadFile(
                            request.getLogo(),
                            "salons");

            salon.setLogoUrl(logoUrl);


            // =================================
            // SAVE
            // =================================

            Salon savedSalon =
                    salonRepository.save(salon);


            return mapToResponse(
                    savedSalon);


        } catch (IOException e) {

            throw new RuntimeException(
                    "Image Upload Failed");

        }
    }


    // =========================================
    // GET ALL SALONS
    // =========================================

    @Override
    public List<SalonResponse>
    getAllSalons() {

        return salonRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }


    // =========================================
    // GET SALON BY ID
    // =========================================

    @Override
    public SalonResponse getSalonById(
            Long id) {

        Salon salon =
                salonRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        return mapToResponse(salon);

    }


    // =========================================
    // UPDATE SALON
    // =========================================

    @Override
    public SalonResponse updateSalon(
            Long id,
            SalonRequest request) {

        try {

            Salon salon =
                    salonRepository
                            .findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Salon Not Found"));


            // =================================
            // UPDATE SALON DETAILS
            // =================================

            salon.setSalonName(
                    request.getSalonName());

            salon.setOwnerName(
                    request.getOwnerName());

            salon.setEmail(
                    request.getEmail());

            salon.setPhoneNumber(
                    request.getPhoneNumber());

            salon.setAddress(
                    request.getAddress());

            salon.setCity(
                    request.getCity());

            salon.setState(
                    request.getState());

            salon.setPincode(
                    request.getPincode());

            salon.setOpeningTime(
                    request.getOpeningTime());

            salon.setClosingTime(
                    request.getClosingTime());

            salon.setDescription(
                    request.getDescription());


            // =================================
            // UPDATE RATING
            // =================================

            if (request.getRating() != null) {

                salon.setRating(
                        request.getRating());

            }


            // =================================
            // UPDATE REVIEW COUNT
            // =================================

            if (request.getReviewCount() != null) {

                salon.setReviewCount(
                        request.getReviewCount());

            }


            // =================================
            // UPDATE STATUS
            // =================================

            salon.setStatus(
                    request.getStatus());


            // =================================
            // UPDATE LOGO
            // =================================

            if (request.getLogo() != null &&
                    !request.getLogo().isEmpty()) {

                String logoUrl =
                        fileUploadUtil.uploadFile(
                                request.getLogo(),
                                "salons");

                salon.setLogoUrl(logoUrl);
            }


            // =================================
            // SAVE
            // =================================

            Salon updatedSalon =
                    salonRepository.save(salon);


            return mapToResponse(
                    updatedSalon);


        } catch (IOException e) {

            throw new RuntimeException(
                    "Image Upload Failed");

        }
    }


    // =========================================
    // DELETE SALON
    // =========================================

    @Override
    public void deleteSalon(
            Long id) {

        Salon salon =
                salonRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        salonRepository.delete(salon);

    }


    // =========================================
    // ENTITY → RESPONSE
    // =========================================

    private SalonResponse mapToResponse(
            Salon salon) {

        SalonResponse response =
                new SalonResponse();


        response.setId(
                salon.getId());

        response.setSalonName(
                salon.getSalonName());

        response.setOwnerName(
                salon.getOwnerName());

        response.setEmail(
                salon.getEmail());

        response.setPhoneNumber(
                salon.getPhoneNumber());

        response.setAddress(
                salon.getAddress());

        response.setCity(
                salon.getCity());

        response.setState(
                salon.getState());

        response.setPincode(
                salon.getPincode());

        response.setOpeningTime(
                salon.getOpeningTime());

        response.setClosingTime(
                salon.getClosingTime());


        // =====================================
        // DESCRIPTION
        // =====================================

        response.setDescription(
                salon.getDescription());


        // =====================================
        // RATING
        // =====================================

        response.setRating(
                salon.getRating());


        // =====================================
        // REVIEW COUNT
        // =====================================

        response.setReviewCount(
                salon.getReviewCount());


        // =====================================
        // LOGO
        // =====================================

        response.setLogoUrl(
                salon.getLogoUrl());


        // =====================================
        // STATUS
        // =====================================

        response.setStatus(
                salon.getStatus());


        return response;

    }

}