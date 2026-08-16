package com.salonmanagementsystem.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonmanagementsystem.enums.ServiceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "service_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================
    // SERVICE DETAILS
    // =========================================

    private String serviceName;

    private String description;

    private Double price;

    private String imageUrl;

    private Integer duration;


    // =========================================
    // SERVICE → CATEGORY
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    @JsonIgnore
    private Category category;


    // =========================================
    // SERVICE → SALON
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salon_id")
    @JsonIgnore
    private Salon salon;


    // =========================================
    // SERVICE STATUS
    // =========================================

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;


    // =========================================
    // SERVICE ↔ EMPLOYEE
    // =========================================

    @ManyToMany(
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "employee_service_table",

            joinColumns = @JoinColumn(
                    name = "service_id"
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "employee_id"
            )
    )
    @JsonIgnore
    private Set<EmployeeClass> employees =
            new HashSet<>();

}