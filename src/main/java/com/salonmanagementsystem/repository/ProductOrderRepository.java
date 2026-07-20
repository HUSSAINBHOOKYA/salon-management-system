package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.ProductOrderClass;

@Repository
public interface ProductOrderRepository
        extends JpaRepository<ProductOrderClass, Long> {
	List<ProductOrderClass> findByCustomerNameContainingIgnoreCase(String customerName);

}