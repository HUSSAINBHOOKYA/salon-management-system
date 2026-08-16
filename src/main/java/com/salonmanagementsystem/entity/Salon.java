package com.salonmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salonmanagementsystem.enums.SalonStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salon_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================
    // SALON DETAILS
    // =========================================

    @Column(nullable = false)
    private String salonName;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private String openingTime;

    @Column(nullable = false)
    private String closingTime;


    // =========================================
    // SALON DESCRIPTION
    // =========================================

    @Column(length = 1000)
    private String description;


    // =========================================
    // SALON RATING
    // =========================================

    private Double rating;

    private Integer reviewCount;


    // =========================================
    // SALON LOGO
    // =========================================

    private String logoUrl;


    // =========================================
    // SALON STATUS
    // =========================================

    @Enumerated(EnumType.STRING)
    private SalonStatus status;

}