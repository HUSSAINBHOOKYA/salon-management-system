package com.salonmanagementsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productOrderTable")
public class ProductOrderClass {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String customerName;

	private String customerPhone;

	private String customerEmail;

	private Integer quantity;

	private Double totalPrice;

	private String paymentStatus;

	private LocalDate orderDate;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductClass product;

}
