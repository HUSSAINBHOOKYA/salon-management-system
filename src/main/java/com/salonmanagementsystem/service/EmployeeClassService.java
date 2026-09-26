package com.salonmanagementsystem.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.constants.AppConstants;
import com.salonmanagementsystem.dto.EmployeeServiceResponse;
import com.salonmanagementsystem.dto.ServiceResponse;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.enums.EmployeeStatus;
import com.salonmanagementsystem.enums.Gender;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.ServiceRepository;

@Service
public class EmployeeClassService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private ServiceRepository serviceRepository;


    // =========================================
    // ADD EMPLOYEE
    // =========================================

    public EmployeeClass addEmployee(
            EmployeeClass employee,
            Long salonId,
            MultipartFile image)
            throws IOException {

        Salon salon =
                salonRepository
                        .findById(salonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        // =====================================
        // CREATE UPLOAD FOLDER
        // =====================================

        File uploadFolder =
                new File(
                        AppConstants.EMPLOYEE_UPLOAD_DIR);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // =====================================
        // SAVE IMAGE
        // =====================================

        if (image != null && !image.isEmpty()) {

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + image.getOriginalFilename();

            Path filePath =
                    Paths.get(
                            AppConstants.EMPLOYEE_UPLOAD_DIR,
                            fileName);

            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            employee.setImageUrl(
                    "/uploads/employees/"
                            + fileName);
        }

        // =====================================
        // DEFAULT RATING
        // =====================================

        if (employee.getRating() == null) {
            employee.setRating(0.0);
        }

        if (employee.getReviewCount() == null) {
            employee.setReviewCount(0);
        }

        // =====================================
        // DEFAULT SERVICES
        // =====================================

        if (employee.getServices() == null) {
            employee.setServices(new HashSet<>());
        }

        // =====================================
        // CONNECT EMPLOYEE → SALON
        // =====================================

        employee.setSalon(salon);

        return employeeRepository.save(employee);
    }


    // =========================================
    // GET ALL EMPLOYEES
    // =========================================

    public List<EmployeeClass> getAllEmployees() {

        return employeeRepository.findAll();
    }


    // =========================================
    // GET EMPLOYEE BY ID
    // =========================================

    public EmployeeClass getEmployeeById(Long id) {

        return employeeRepository
                .findById(id)
                .orElse(null);
    }


    // =========================================
    // GET EMPLOYEES BY SALON
    // =========================================

    public List<EmployeeClass> getEmployeesBySalon(
            Long salonId) {

        salonRepository
                .findById(salonId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Salon Not Found"));

        return employeeRepository
                .findBySalonId(salonId);
    }


    // =========================================
    // UPDATE EMPLOYEE
    // =========================================

    public EmployeeClass updateEmployee(
            Long id,
            EmployeeClass employee,
            Long salonId,
            MultipartFile image)
            throws IOException {

        EmployeeClass existingEmployee =
                employeeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        Salon salon =
                salonRepository
                        .findById(salonId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Salon Not Found"));

        // =====================================
        // UPDATE BASIC DETAILS
        // =====================================

        existingEmployee.setEmployeeName(
                employee.getEmployeeName());

        existingEmployee.setEmployeeEmail(
                employee.getEmployeeEmail());

        existingEmployee.setSpecialization(
                employee.getSpecialization());

        existingEmployee.setPhoneNumber(
                employee.getPhoneNumber());

        existingEmployee.setExperience(
                employee.getExperience());

        existingEmployee.setSalary(
                employee.getSalary());

        existingEmployee.setDescription(
                employee.getDescription());

        // =====================================
        // UPDATE RATING
        // =====================================

        if (employee.getRating() != null) {

            existingEmployee.setRating(
                    employee.getRating());
        }

        if (employee.getReviewCount() != null) {

            existingEmployee.setReviewCount(
                    employee.getReviewCount());
        }

        // =====================================
        // UPDATE STATUS
        // =====================================

        existingEmployee.setStatus(
                employee.getStatus());

        // =====================================
        // UPDATE SALON
        // =====================================

        existingEmployee.setSalon(salon);

        // =====================================
        // UPDATE IMAGE
        // =====================================

        if (image != null && !image.isEmpty()) {

            File uploadFolder =
                    new File(
                            AppConstants.EMPLOYEE_UPLOAD_DIR);

            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + image.getOriginalFilename();

            Path filePath =
                    Paths.get(
                            AppConstants.EMPLOYEE_UPLOAD_DIR,
                            fileName);

            Files.copy(
                    image.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            existingEmployee.setImageUrl(
                    "/uploads/employees/"
                            + fileName);
        }

        return employeeRepository
                .save(existingEmployee);
    }


    // =========================================
    // DELETE EMPLOYEE
    // =========================================

    public String deleteEmployee(Long id) {

        EmployeeClass employee =
                employeeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        // =====================================
        // DELETE IMAGE
        // =====================================

        if (employee.getImageUrl() != null &&
                !employee.getImageUrl().isBlank()) {

            String imagePath =
                    employee.getImageUrl()
                            .replace(
                                    "/uploads/",
                                    "uploads/");

            File imageFile =
                    new File(imagePath);

            if (imageFile.exists()) {

                imageFile.delete();
            }
        }

        // =====================================
        // DELETE EMPLOYEE
        // =====================================

        employeeRepository.delete(employee);

        return "Employee Deleted Successfully";
    }


    // =========================================
    // SEARCH EMPLOYEE
    // =========================================

    public List<EmployeeClass> searchEmployee(
            String name) {

        return employeeRepository
                .findByEmployeeNameContainingIgnoreCase(
                        name);
    }


    // =========================================
    // PAGINATION
    // =========================================

    public Page<EmployeeClass> getEmployeesPagination(
            int page,
            int size) {

        return employeeRepository.findAll(
                PageRequest.of(page, size));
    }


    // =========================================
    // FILTER BY STATUS
    // =========================================

    public List<EmployeeClass> getEmployeesByStatus(
            EmployeeStatus status) {

        return employeeRepository
                .findByStatus(status);
    }


    // =========================================
    // TOP RATED EMPLOYEES BY GENDER
    // =========================================

    public List<EmployeeClass> getTopRatedEmployeesByGender(
            Gender gender) {

        return employeeRepository
                .findDistinctByServices_GenderAndStatusOrderByRatingDesc(
                        gender,
                        EmployeeStatus.ACTIVE);
    }


    // =========================================
    // ASSIGN SERVICES TO EMPLOYEE
    // =========================================

    public EmployeeClass assignServices(
            Long employeeId,
            List<Long> serviceIds) {

        // =====================================
        // FIND EMPLOYEE
        // =====================================

        EmployeeClass employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        // =====================================
        // EMPLOYEE MUST HAVE SALON
        // =====================================

        Salon employeeSalon =
                employee.getSalon();

        if (employeeSalon == null) {

            throw new RuntimeException(
                    "Employee is not assigned to a salon");
        }

        Long employeeSalonId =
                employeeSalon.getId();

        // =====================================
        // VALIDATE SERVICE IDS
        // =====================================

        if (serviceIds == null ||
                serviceIds.isEmpty()) {

            throw new RuntimeException(
                    "At least one service is required");
        }

        // =====================================
        // FIND SERVICES
        // =====================================

        List<SalonService> services =
                serviceRepository
                        .findAllById(serviceIds);

        // =====================================
        // CHECK ALL SERVICES EXIST
        // =====================================

        if (services.size() != serviceIds.size()) {

            throw new RuntimeException(
                    "One or more services not found");
        }

        // =====================================
        // CHECK SERVICES BELONG TO
        // EMPLOYEE'S SALON
        // =====================================

        for (SalonService service : services) {

            if (service.getSalon() == null) {

                throw new RuntimeException(
                        "Service is not assigned to a salon");
            }

            Long serviceSalonId =
                    service.getSalon().getId();

            if (!employeeSalonId.equals(
                    serviceSalonId)) {

                throw new RuntimeException(
                        "Service does not belong to employee salon");
            }
        }

        // =====================================
        // ASSIGN ONLY SELECTED SERVICES
        // =====================================

        employee.setServices(
                new HashSet<>(services));

        // =====================================
        // SAVE EMPLOYEE
        // =====================================

        return employeeRepository.save(employee);
    }


    // =========================================
    // GET SERVICES ASSIGNED TO EMPLOYEE
    // =========================================

    public List<ServiceResponse> getEmployeeServices(
            Long employeeId) {

        // =====================================
        // FIND EMPLOYEE
        // =====================================

        EmployeeClass employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee Not Found"));

        // =====================================
        // GET ASSIGNED SERVICES
        // =====================================

        return employee.getServices()
                .stream()
                .map(service -> {

                    ServiceResponse response =
                            new ServiceResponse();

                    // =================================
                    // SERVICE DETAILS
                    // =================================

                    response.setId(
                            service.getId());

                    response.setServiceName(
                            service.getServiceName());

                    response.setDescription(
                            service.getDescription());

                    response.setPrice(
                            service.getPrice());

                    response.setDuration(
                            service.getDuration());

                    response.setImageUrl(
                            service.getImageUrl());

                    // =================================
                    // GENDER
                    // =================================

                    response.setGender(
                            service.getGender());

                    response.setStatus(
                            service.getStatus());

                    // =================================
                    // CATEGORY
                    // =================================

                    if (service.getCategory() != null) {

                        response.setCategoryId(
                                service.getCategory()
                                        .getId());

                        response.setCategoryName(
                                service.getCategory()
                                        .getCategoryName());
                    }

                    // =================================
                    // SALON
                    // =================================

                    if (service.getSalon() != null) {

                        response.setSalonId(
                                service.getSalon()
                                        .getId());

                        response.setSalonName(
                                service.getSalon()
                                        .getSalonName());
                    }

                    return response;

                })
                .toList();
    }
    
 // =========================================
 // ADMIN - GET ALL EMPLOYEES WITH SALON
 // =========================================

 @Transactional(readOnly = true)
 public List<EmployeeServiceResponse> getAdminAllEmployees() {

     return employeeRepository
             .findAll()
             .stream()
             .map(employee -> {

                 EmployeeServiceResponse response =
                         new EmployeeServiceResponse();

                 // =====================================
                 // EMPLOYEE ID
                 // =====================================

                 response.setId(
                         employee.getId());

                 // =====================================
                 // EMPLOYEE DETAILS
                 // =====================================

                 response.setEmployeeName(
                         employee.getEmployeeName());

                 response.setEmployeeEmail(
                         employee.getEmployeeEmail());

                 response.setSpecialization(
                         employee.getSpecialization());

                 response.setPhoneNumber(
                         employee.getPhoneNumber());

                 response.setExperience(
                         employee.getExperience());

                 response.setSalary(
                         employee.getSalary());

                 response.setDescription(
                         employee.getDescription());

                 // =====================================
                 // RATING
                 // =====================================

                 response.setRating(
                         employee.getRating());

                 response.setReviewCount(
                         employee.getReviewCount());

                 // =====================================
                 // IMAGE
                 // =====================================

                 response.setImageUrl(
                         employee.getImageUrl());

                 // =====================================
                 // STATUS
                 // =====================================

                 response.setStatus(
                         employee.getStatus());

                 // =====================================
                 // SALON
                 // =====================================

                 if (employee.getSalon() != null) {

                     response.setSalonId(
                             employee.getSalon().getId());

                     response.setSalonName(
                             employee.getSalon().getSalonName());
                 }

                 return response;
             })
             .toList();
 }

}