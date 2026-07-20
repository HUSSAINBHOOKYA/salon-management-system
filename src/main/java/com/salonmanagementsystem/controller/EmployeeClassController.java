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

import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.service.EmployeeClassService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeClassController {

    @Autowired
    private EmployeeClassService employeeClassService;

    // Add Employee
    @PostMapping("/add")
    public EmployeeClass addEmployee(@RequestBody EmployeeClass employeeClass) {

        return employeeClassService.addEmployee(employeeClass);
    }

    // Get All Employees
    @GetMapping("/all")
    public List<EmployeeClass> getAllEmployees() {

        return employeeClassService.getAllEmployees();
    }

    // Get Employee By Id
    @GetMapping("/{id}")
    public EmployeeClass getEmployeeById(@PathVariable Long id) {

        return employeeClassService.getEmployeeById(id);
    }

    // Update Employee
    @PutMapping("/update/{id}")
    public EmployeeClass updateEmployee(@PathVariable Long id,
                                        @RequestBody EmployeeClass employeeClass) {

        return employeeClassService.updateEmployee(id, employeeClass);
    }

    // Delete Employee
    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {

        return employeeClassService.deleteEmployee(id);
    }
    
    @GetMapping("/search")
    public List<EmployeeClass> searchEmployee(@RequestParam String name) {
        return employeeClassService.searchEmployee(name);
    }

}