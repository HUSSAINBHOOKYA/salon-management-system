package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.ApiResponse;
import com.salonmanagementsystem.dto.SalonRequest;
import com.salonmanagementsystem.dto.SalonResponse;
import com.salonmanagementsystem.service.SalonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/salons")
@CrossOrigin("*")
public class SalonController {

    private final SalonService salonService;


    public SalonController(
            SalonService salonService) {

        this.salonService =
                salonService;
    }


    // =========================================
    // ADD SALON
    // =========================================

    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<
            ApiResponse<SalonResponse>>
    addSalon(
            @Valid
            @ModelAttribute SalonRequest request) {


        SalonResponse response =
                salonService.addSalon(request);


        return ResponseEntity.ok(

                new ApiResponse<>(

                        true,

                        "Salon Created Successfully",

                        response

                )

        );
    }


    // =========================================
    // GET ALL SALONS
    // =========================================

    @GetMapping
    public ResponseEntity<
            List<SalonResponse>>
    getAllSalons() {

        return ResponseEntity.ok(
                salonService.getAllSalons());

    }


    // =========================================
    // GET SALON BY ID
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<
            SalonResponse>
    getSalonById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                salonService.getSalonById(id));

    }


    // =========================================
    // UPDATE SALON
    // =========================================

    @PutMapping(
            value = "/{id}",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<
            SalonResponse>
    updateSalon(

            @PathVariable Long id,

            @ModelAttribute SalonRequest request) {

        return ResponseEntity.ok(
                salonService.updateSalon(
                        id,
                        request));

    }


    // =========================================
    // DELETE SALON
    // =========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteSalon(
            @PathVariable Long id) {

        salonService.deleteSalon(id);

        return ResponseEntity.ok(
                "Salon Deleted Successfully");

    }

}