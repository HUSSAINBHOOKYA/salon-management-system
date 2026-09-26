package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.dto.EmployeeServiceResponse;
import com.salonmanagementsystem.dto.ServiceRequest;
import com.salonmanagementsystem.dto.ServiceResponse;
import com.salonmanagementsystem.enums.Gender;
import com.salonmanagementsystem.service.ServiceService;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService serviceService;


    public ServiceController(
            ServiceService serviceService
    ) {

        this.serviceService =
                serviceService;
    }


    // =========================================
    // ADD SERVICE
    // =========================================

    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ServiceResponse>
    addService(
            @ModelAttribute ServiceRequest request
    ) {

        return ResponseEntity.ok(
                serviceService.addService(request)
        );
    }


    // =========================================
    // GET ALL SERVICES
    // =========================================

    @GetMapping
    public ResponseEntity<List<ServiceResponse>>
    getAllServices() {

        return ResponseEntity.ok(
                serviceService.getAllServices()
        );
    }


    // =========================================
    // GET SERVICES BY GENDER
    // =========================================

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<ServiceResponse>>
    getServicesByGender(
            @PathVariable Gender gender
    ) {

        return ResponseEntity.ok(
                serviceService.getServicesByGender(
                        gender
                )
        );
    }


    // =========================================
    // GET SERVICE BY ID
    // =========================================

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse>
    getServiceById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                serviceService.getServiceById(id)
        );
    }


    // =========================================
    // GET SERVICES BY SALON
    // =========================================

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<ServiceResponse>>
    getServicesBySalon(
            @PathVariable Long salonId
    ) {

        return ResponseEntity.ok(
                serviceService
                        .getServicesBySalon(salonId)
        );
    }


    // =========================================
    // GET SERVICES BY EMPLOYEE
    // =========================================

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ServiceResponse>>
    getServicesByEmployee(
            @PathVariable Long employeeId
    ) {

        return ResponseEntity.ok(
                serviceService
                        .getServicesByEmployee(
                                employeeId
                        )
        );
    }


    // =========================================
    // GET EMPLOYEES BY SERVICE
    // =========================================

    @GetMapping("/{serviceId}/employees")
    public ResponseEntity<List<EmployeeServiceResponse>>
    getEmployeesByService(
            @PathVariable Long serviceId
    ) {

        return ResponseEntity.ok(
                serviceService
                        .getEmployeesByService(
                                serviceId
                        )
        );
    }


    // =========================================
    // UPDATE SERVICE
    // =========================================

    @PutMapping(
            value = "/{id}",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ServiceResponse>
    updateService(
            @PathVariable Long id,
            @ModelAttribute ServiceRequest request
    ) {

        return ResponseEntity.ok(
                serviceService.updateService(
                        id,
                        request
                )
        );
    }


    // =========================================
    // DELETE SERVICE
    // =========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteService(
            @PathVariable Long id
    ) {

        serviceService.deleteService(id);

        return ResponseEntity.ok(
                "Service deleted successfully."
        );
    }

}