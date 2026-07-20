package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.entity.ServiceClass;
import com.salonmanagementsystem.service.ServiceClassService;

@RestController
@RequestMapping("/api/services")
public class ServiceClassController {

    @Autowired
    private ServiceClassService serviceClassService;

    // Add Service
    @PostMapping("/add")
    public ServiceClass addService(@RequestBody ServiceClass serviceClass) {
        return serviceClassService.addService(serviceClass);
    }

    // Get All Services
    @GetMapping("/all")
    public List<ServiceClass> getAllServices() {
        return serviceClassService.getAllServices();
    }

    // Get Service By Id
    @GetMapping("/{id}")
    public ServiceClass getServiceById(@PathVariable Long id) {
        return serviceClassService.getServiceById(id);
    }

    // Update Service
    @PutMapping("/update/{id}")
    public ServiceClass updateService(@PathVariable Long id,
                                      @RequestBody ServiceClass serviceClass) {
        return serviceClassService.updateService(id, serviceClass);
    }

    // Delete Service
    @DeleteMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        return serviceClassService.deleteService(id);
    }
    @GetMapping("/search")
    public List<ServiceClass> searchService(@RequestParam String name) {
        return serviceClassService.searchService(name);
    }
}