package com.salonmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.BillClass;

@Repository
public interface BillRepository extends JpaRepository<BillClass, Long> {
	List<BillClass> findByPaymentStatus(String paymentStatus);

}