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
@Table(name = "productTable")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductClass {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String productName;


    private String description;


    private String category;


    private String brand;


    private Double price;


    private Integer stockQuantity;


    private String imageUrl;


    private String status;

}