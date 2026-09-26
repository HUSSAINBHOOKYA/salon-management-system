	package com.salonmanagementsystem.dto;
	
	import java.time.LocalDate;
	
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class AppointmentResponseDTO {
	
	    private Long appointmentId;
	
	    private String customerName;
	
	    private String customerPhone;
	
	    private String customerEmail;
	
	    private String serviceName;
	
	    private String employeeName;
	
	    private String employeeImage;
	
	    private Long salonId;
	
	    private String salonName;
	
	    private LocalDate appointmentDate;
	
	    private String appointmentTime;
	
	    private String bookingStatus;
	
	    private String paymentStatus;
	
	    private Double totalPrice;
	
	    private Integer totalDuration;
	}