package com.salonmanagementsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.SalonRequest;
import com.salonmanagementsystem.dto.SalonResponse;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.enums.SalonStatus;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.util.FileUploadUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    private final FileUploadUtil fileUploadUtil;

    @PersistenceContext
    private EntityManager entityManager;


    public SalonServiceImpl(
            SalonRepository salonRepository,
            FileUploadUtil fileUploadUtil) {

        this.salonRepository = salonRepository;
        this.fileUploadUtil = fileUploadUtil;
    }


    // =========================================
    // ADD SALON
    // ADMIN
    // =========================================

    @Override
    public SalonResponse addSalon(
            SalonRequest request) {

        try {

            Salon salon = new Salon();

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
                    "Image Upload Failed",
                    e);
        }
    }


    // =========================================
    // PARTNER - SUBMIT SALON APPLICATION
    // =========================================

    @Override
    public SalonResponse addPartnerSalon(
            SalonRequest request,
            String partnerEmail) {

        try {

            Salon salon = new Salon();

            salon.setSalonName(
                    request.getSalonName());

            salon.setOwnerName(
                    request.getOwnerName());


            // =================================
            // SALON CONTACT EMAIL
            // =================================

            salon.setEmail(
                    request.getEmail());


            // =================================
            // PARTNER ACCOUNT EMAIL
            // =================================

            salon.setPartnerEmail(
                    partnerEmail);

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
            // INITIAL RATING
            // =================================

            salon.setRating(0.0);

            salon.setReviewCount(0);


            // =================================
            // PARTNER APPLICATION STATUS
            // =================================

            salon.setStatus(
                    SalonStatus.PENDING);


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
                    "Image Upload Failed",
                    e);
        }
    }


    // =========================================
    // PARTNER - APPLICATION STATUS
    // =========================================

    @Override
    public SalonResponse getPartnerApplicationStatus(
            String partnerEmail) {

        Salon salon =
                salonRepository
                        .findByPartnerEmail(partnerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Partner salon application not found"));

        return mapToResponse(salon);
    }


    // =========================================
    // GET ALL SALONS
    // =========================================

    @Override
    public List<SalonResponse> getAllSalons() {

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

            if (request.getLogo() != null
                    && !request.getLogo().isEmpty()) {

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
                    "Image Upload Failed",
                    e);
        }
    }


    // =========================================
    // DELETE SALON
    // =========================================
    //
    // PHYSICAL DELETE
    //
    // Deletes dependent records in the correct
    // foreign-key order before deleting the salon.
    //
    // =========================================

    @Override
    @Transactional
    public void deleteSalon(Long id) {

        // =========================================
        // CHECK SALON
        // =========================================

        salonRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salon Not Found"));


        // =========================================
        // 1. DELETE BILLS CONNECTED TO
        //    APPOINTMENTS OF THIS SALON
        // =========================================

        entityManager.createNativeQuery("""
                DELETE b
                FROM bill_table b
                INNER JOIN appointment_table a
                    ON b.appointment_id = a.id
                WHERE a.salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 2. DELETE BILLS CONNECTED TO
        //    PRODUCT ORDERS OF THIS SALON
        // =========================================

        entityManager.createNativeQuery("""
                DELETE b
                FROM bill_table b
                INNER JOIN product_order_table po
                    ON b.product_order_id = po.id
                INNER JOIN product_table p
                    ON po.product_id = p.id
                WHERE p.salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 3. DELETE APPOINTMENT → SERVICE
        //    JOIN TABLE RECORDS
        // =========================================

        entityManager.createNativeQuery("""
                DELETE aps
                FROM appointment_service_table aps
                INNER JOIN appointment_table a
                    ON aps.appointment_id = a.id
                WHERE a.salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 4. DELETE APPOINTMENTS
        // =========================================

        entityManager.createNativeQuery("""
                DELETE FROM appointment_table
                WHERE salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 5. DELETE TIME SLOTS
        // =========================================

        entityManager.createNativeQuery("""
                DELETE FROM time_slot_table
                WHERE salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 6. DELETE EMPLOYEE → SERVICE
        //    JOIN TABLE RECORDS
        // =========================================

        entityManager.createNativeQuery("""
                DELETE es
                FROM employee_service_table es
                INNER JOIN employee_table e
                    ON es.employee_id = e.id
                WHERE e.salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 7. DELETE SERVICE → EMPLOYEE
        //    JOIN TABLE RECORDS
        //
        // This handles services belonging to
        // the salon even if a relationship exists
        // that was not removed by employee_id.
        // =========================================

        entityManager.createNativeQuery("""
                DELETE es
                FROM employee_service_table es
                INNER JOIN service_table s
                    ON es.service_id = s.id
                WHERE s.salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 8. DELETE PRODUCT ORDERS
        //    BELONGING TO SALON PRODUCTS
        // =========================================

        entityManager.createNativeQuery("""
                DELETE po
                FROM product_order_table po
                INNER JOIN product_table p
                    ON po.product_id = p.id
                WHERE p.salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 9. DELETE PRODUCTS
        // =========================================

        entityManager.createNativeQuery("""
                DELETE FROM product_table
                WHERE salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 10. DELETE EMPLOYEES
        // =========================================

        entityManager.createNativeQuery("""
                DELETE FROM employee_table
                WHERE salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 11. DELETE SERVICES
        // =========================================

        entityManager.createNativeQuery("""
                DELETE FROM service_table
                WHERE salon_id = :salonId
                """)
                .setParameter(
                        "salonId",
                        id)
                .executeUpdate();


        // =========================================
        // 12. FLUSH / CLEAR
        // =========================================

        entityManager.flush();

        entityManager.clear();


        // =========================================
        // 13. DELETE SALON
        // =========================================

        Salon salonToDelete =
                salonRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        salonRepository.delete(
                salonToDelete);


        // =========================================
        // 14. FLUSH
        // =========================================

        entityManager.flush();
    }


    // =========================================
    // ADMIN - GET PENDING SALONS
    // =========================================

    @Override
    public List<SalonResponse> getPendingSalons() {

        return salonRepository
                .findByStatus(SalonStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // =========================================
    // ADMIN - APPROVE SALON
    // =========================================

    @Override
    public SalonResponse approveSalon(
            Long id) {

        Salon salon =
                salonRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        salon.setStatus(
                SalonStatus.ACTIVE);

        Salon updatedSalon =
                salonRepository.save(salon);

        return mapToResponse(
                updatedSalon);
    }


    // =========================================
    // ADMIN - REJECT SALON
    // =========================================

    @Override
    public SalonResponse rejectSalon(
            Long id) {

        Salon salon =
                salonRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        salon.setStatus(
                SalonStatus.REJECTED);

        Salon updatedSalon =
                salonRepository.save(salon);

        return mapToResponse(
                updatedSalon);
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

        response.setDescription(
                salon.getDescription());

        response.setRating(
                salon.getRating());

        response.setReviewCount(
                salon.getReviewCount());

        response.setLogoUrl(
                salon.getLogoUrl());

        response.setStatus(
                salon.getStatus());

        return response;
    }
}