package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.ServiceClass;
import com.salonmanagementsystem.repository.ServiceRepository;

@Service
public class ServiceClassService {

    @Autowired
    private ServiceRepository serviceRepository;

    // Add Service
    public ServiceClass addService(ServiceClass serviceClass) {
        return serviceRepository.save(serviceClass);
    }

    // Get All Services
    public List<ServiceClass> getAllServices() {
        return serviceRepository.findAll();
    }

    // Get Service By Id
    public ServiceClass getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    // Update Service
    public ServiceClass updateService(Long id, ServiceClass serviceClass) {

        ServiceClass existingService = serviceRepository.findById(id).orElse(null);

        if (existingService != null) {
            existingService.setServiceName(serviceClass.getServiceName());
            existingService.setDescription(serviceClass.getDescription());
            existingService.setPrice(serviceClass.getPrice());
            existingService.setDuration(serviceClass.getDuration());

            return serviceRepository.save(existingService);
        }

        return null;
    }

    // Delete Service
    public String deleteService(Long id) {

        serviceRepository.deleteById(id);

        return "Service Deleted Successfully";
    }
    public List<ServiceClass> searchService(String name) {
        return serviceRepository.findByServiceNameContainingIgnoreCase(name);
    }
}