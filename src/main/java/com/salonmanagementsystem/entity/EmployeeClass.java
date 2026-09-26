package com.salonmanagementsystem.entity;

import java.util.HashSet;


import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salonmanagementsystem.enums.EmployeeStatus;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "employee_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================
    // EMPLOYEE DETAILS
    // =========================================

    private String employeeName;

    private String employeeEmail;

    private String specialization;

    private String phoneNumber;

    private Integer experience;

    private Double salary;

    private String description;

    // =========================================
    // EMPLOYEE RATING
    // =========================================

    private Double rating;

    private Integer reviewCount;

    // =========================================
    // EMPLOYEE STATUS
    // =========================================

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    // =========================================
    // EMPLOYEE IMAGE
    // =========================================

    private String imageUrl;

    // =========================================
    // EMPLOYEE → SALON
    // =========================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "salon_id",
            nullable = false
    )
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Salon salon;

    // =========================================
    // EMPLOYEE ↔ SERVICES
    // =========================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_service_table",

            joinColumns = @JoinColumn(
                    name = "employee_id"
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "service_id"
            )
    )
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SalonService> services =
            new HashSet<>();

}