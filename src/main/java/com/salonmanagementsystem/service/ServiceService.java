package com.salonmanagementsystem.service;

import java.util.List;

import com.salonmanagementsystem.dto.EmployeeServiceResponse;
import com.salonmanagementsystem.dto.ServiceRequest;
import com.salonmanagementsystem.dto.ServiceResponse;
import com.salonmanagementsystem.enums.Gender;

public interface ServiceService {

    // =========================================
    // ADD SERVICE
    // =========================================

    ServiceResponse addService(
            ServiceRequest request
    );


    // =========================================
    // GET ALL SERVICES
    // =========================================

    List<ServiceResponse>
    getAllServices();


    // =========================================
    // GET SERVICES BY SALON
    // =========================================

    List<ServiceResponse>
    getServicesBySalon(
            Long salonId
    );


    // =========================================
    // GET SERVICES BY EMPLOYEE
    // =========================================

    List<ServiceResponse>
    getServicesByEmployee(
            Long employeeId
    );


    // =========================================
    // GET SERVICES BY GENDER
    // =========================================

    List<ServiceResponse>
    getServicesByGender(
            Gender gender
    );


    // =========================================
    // GET SERVICE BY ID
    // =========================================

    ServiceResponse getServiceById(
            Long id
    );


    // =========================================
    // UPDATE SERVICE
    // =========================================

    ServiceResponse updateService(
            Long id,
            ServiceRequest request
    );


    // =========================================
    // DELETE SERVICE
    // =========================================

    void deleteService(
            Long id
    );


    // =========================================
    // GET EMPLOYEES BY SERVICE
    // =========================================

    List<EmployeeServiceResponse>
    getEmployeesByService(
            Long serviceId
    );

}