package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.repository.EmployeeRepository;

@Service
public class EmployeeClassService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Add Employee
    public EmployeeClass addEmployee(EmployeeClass employeeClass) {
        return employeeRepository.save(employeeClass);
    }

    // Get All Employees
    public List<EmployeeClass> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get Employee By Id
    public EmployeeClass getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    // Update Employee
    public EmployeeClass updateEmployee(Long id, EmployeeClass employeeClass) {

        EmployeeClass existingEmployee = employeeRepository.findById(id).orElse(null);

        if (existingEmployee != null) {

            existingEmployee.setEmployeeName(employeeClass.getEmployeeName());
            existingEmployee.setEmployeeEmail(employeeClass.getEmployeeEmail());
            existingEmployee.setSpecialization(employeeClass.getSpecialization());
            existingEmployee.setPhoneNumber(employeeClass.getPhoneNumber());
            existingEmployee.setExperience(employeeClass.getExperience());
            existingEmployee.setSalary(employeeClass.getSalary());
            existingEmployee.setStatus(employeeClass.getStatus());
            existingEmployee.setImageUrl(employeeClass.getImageUrl());

            return employeeRepository.save(existingEmployee);
        }

        return null;
    }

    // Delete Employee
    public String deleteEmployee(Long id) {

        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return "Employee Deleted Successfully";
        }

        return "Employee Not Found";
    }
    
      public List<EmployeeClass> searchEmployee(String name) {
        return employeeRepository.findByEmployeeNameContainingIgnoreCase(name);
    }

}