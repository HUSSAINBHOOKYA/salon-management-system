package com.salonmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salonmanagementsystem.enums.ProductStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================
    // PRODUCT DETAILS
    // =========================================

    private String productName;

    private String description;

    private String category;

    private String brand;

    private Double price;

    private Integer stockQuantity;


    // =========================================
    // PRODUCT RATING
    // =========================================

    private Double rating;

    private Integer reviewCount;


    // =========================================
    // PRODUCT IMAGE
    // =========================================

    private String imageUrl;


    // =========================================
    // PRODUCT STATUS
    // =========================================

    @Enumerated(EnumType.STRING)
    private ProductStatus status;


    // =========================================
    // PRODUCT → SALON
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "salon_id",
            nullable = false
    )
    @JsonIgnoreProperties({
            "hibernateLazyInitializer",
            "handler"
    })
    private Salon salon;

}