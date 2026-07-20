package com.salonmanagementsystem.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.ProductClass;


@Repository
public interface ProductRepository extends JpaRepository<ProductClass, Long> {
	List<ProductClass> findByProductNameContainingIgnoreCase(String productName);

}