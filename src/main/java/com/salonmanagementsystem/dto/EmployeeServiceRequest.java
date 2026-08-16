package com.salonmanagementsystem.dto;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeServiceRequest {

    private List<Long> serviceIds;

}