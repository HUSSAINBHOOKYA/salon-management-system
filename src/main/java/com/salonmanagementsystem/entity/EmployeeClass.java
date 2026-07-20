package com.salonmanagementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employeeTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;
    private String employeeEmail;

    private String specialization;

    private String phoneNumber;

    private Integer experience;

    private Double salary;

    private String status;

    private String imageUrl;
}
