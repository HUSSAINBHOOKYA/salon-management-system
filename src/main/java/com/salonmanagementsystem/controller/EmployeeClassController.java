package com.salonmanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.salonmanagementsystem.dto.EmployeeServiceRequest;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.enums.EmployeeStatus;
import com.salonmanagementsystem.service.EmployeeClassService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeClassController {

    @Autowired
    private EmployeeClassService employeeClassService;


    // =========================================
    // ADD EMPLOYEE
    // =========================================

    @PostMapping(
            value = "/add",
            consumes = "multipart/form-data"
    )
    public EmployeeClass addEmployee(

            @RequestParam String employeeName,

            @RequestParam String employeeEmail,

            @RequestParam String specialization,

            @RequestParam String phoneNumber,

            @RequestParam Integer experience,

            @RequestParam Double salary,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            Double rating,

            @RequestParam(required = false)
            Integer reviewCount,

            @RequestParam EmployeeStatus status,

            @RequestParam Long salonId,

            @RequestParam(required = false)
            MultipartFile image

    ) throws IOException {


        EmployeeClass employee =
                new EmployeeClass();


        employee.setEmployeeName(
                employeeName
        );

        employee.setEmployeeEmail(
                employeeEmail
        );

        employee.setSpecialization(
                specialization
        );

        employee.setPhoneNumber(
                phoneNumber
        );

        employee.setExperience(
                experience
        );

        employee.setSalary(
                salary
        );

        employee.setDescription(
                description
        );

        employee.setRating(
                rating
        );

        employee.setReviewCount(
                reviewCount
        );

        employee.setStatus(
                status
        );


        return employeeClassService.addEmployee(
                employee,
                salonId,
                image
        );

    }


    // =========================================
    // GET ALL EMPLOYEES
    // =========================================

    @GetMapping("/all")
    public List<EmployeeClass> getAllEmployees() {

        return employeeClassService
                .getAllEmployees();

    }


    // =========================================
    // GET EMPLOYEE BY ID
    // =========================================

    @GetMapping("/{id}")
    public EmployeeClass getEmployeeById(
            @PathVariable Long id) {

        return employeeClassService
                .getEmployeeById(id);

    }


    // =========================================
    // GET EMPLOYEES BY SALON
    // =========================================

    @GetMapping("/salon/{salonId}")
    public List<EmployeeClass> getEmployeesBySalon(
            @PathVariable Long salonId) {

        return employeeClassService
                .getEmployeesBySalon(salonId);

    }


    // =========================================
    // UPDATE EMPLOYEE
    // =========================================

    @PutMapping(
            value = "/update/{id}",
            consumes = "multipart/form-data"
    )
    public EmployeeClass updateEmployee(

            @PathVariable Long id,

            @RequestParam String employeeName,

            @RequestParam String employeeEmail,

            @RequestParam String specialization,

            @RequestParam String phoneNumber,

            @RequestParam Integer experience,

            @RequestParam Double salary,

            @RequestParam(required = false)
            String description,

            @RequestParam(required = false)
            Double rating,

            @RequestParam(required = false)
            Integer reviewCount,

            @RequestParam EmployeeStatus status,

            @RequestParam Long salonId,

            @RequestParam(required = false)
            MultipartFile image

    ) throws IOException {


        EmployeeClass employee =
                new EmployeeClass();


        employee.setEmployeeName(
                employeeName
        );

        employee.setEmployeeEmail(
                employeeEmail
        );

        employee.setSpecialization(
                specialization
        );

        employee.setPhoneNumber(
                phoneNumber
        );

        employee.setExperience(
                experience
        );

        employee.setSalary(
                salary
        );

        employee.setDescription(
                description
        );

        employee.setRating(
                rating
        );

        employee.setReviewCount(
                reviewCount
        );

        employee.setStatus(
                status
        );


        return employeeClassService.updateEmployee(
                id,
                employee,
                salonId,
                image
        );

    }


    // =========================================
    // DELETE EMPLOYEE
    // =========================================

    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id) {

        return employeeClassService
                .deleteEmployee(id);

    }


    // =========================================
    // SEARCH EMPLOYEE
    // =========================================

    @GetMapping("/search")
    public List<EmployeeClass> searchEmployee(
            @RequestParam String name) {

        return employeeClassService
                .searchEmployee(name);

    }


    // =========================================
    // PAGINATION
    // =========================================

    @GetMapping("/pagination")
    public Page<EmployeeClass> getEmployeesPagination(

            @RequestParam int page,

            @RequestParam int size

    ) {

        return employeeClassService
                .getEmployeesPagination(
                        page,
                        size
                );

    }


    // =========================================
    // FILTER BY STATUS
    // =========================================

    @GetMapping("/status/{status}")
    public List<EmployeeClass> getEmployeesByStatus(

            @PathVariable EmployeeStatus status

    ) {

        return employeeClassService
                .getEmployeesByStatus(status);

    }


    // =========================================
    // ASSIGN SERVICES TO EMPLOYEE
    // =========================================



   @PutMapping("/{employeeId}/services")
    public EmployeeClass assignServices(

         @PathVariable Long employeeId,

         @RequestBody EmployeeServiceRequest request

   ) {

        return employeeClassService
             .assignServices(
                     employeeId,
                     request.getServiceIds()
             );
 }
   
// =========================================
// GET SERVICES ASSIGNED TO EMPLOYEE
// =========================================

@GetMapping("/{employeeId}/services")
public List<SalonService> getEmployeeServices(
        @PathVariable Long employeeId) {

    return employeeClassService
            .getEmployeeServices(employeeId);
}

}