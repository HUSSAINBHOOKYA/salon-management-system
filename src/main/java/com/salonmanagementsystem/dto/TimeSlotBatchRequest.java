package com.salonmanagementsystem.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotBatchRequest {

    private Long salonId;

    private Long employeeId;

    private LocalDate slotDate;

    private List<String> slotTimes;
}