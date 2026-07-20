package com.salonmanagementsystem.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salonmanagementsystem.entity.ServiceClass;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceClass, Long> {
	List<ServiceClass> findByServiceNameContainingIgnoreCase(String serviceName);
}
