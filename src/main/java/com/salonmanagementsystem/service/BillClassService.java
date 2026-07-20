package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.BillRepository;
import com.salonmanagementsystem.repository.ProductOrderRepository;

@Service
public class BillClassService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    // Add Bill
    public BillClass addBill(BillClass billClass) {

        AppointmentClass appointment = appointmentRepository
                .findById(billClass.getAppointment().getId())
                .orElseThrow(() -> new RuntimeException("Appointment Not Found"));

        ProductOrderClass productOrder = productOrderRepository
                .findById(billClass.getProductOrder().getId())
                .orElseThrow(() -> new RuntimeException("Product Order Not Found"));

        billClass.setAppointment(appointment);
        billClass.setProductOrder(productOrder);

        // Service Amount
        Double serviceAmount = appointment.getService().getPrice();

        // Product Amount
        Double productAmount = productOrder.getTotalPrice();

        // Total Amount
        Double totalAmount = serviceAmount + productAmount;

        billClass.setCustomerName(appointment.getCustomerName());
        billClass.setServiceAmount(serviceAmount);
        billClass.setProductAmount(productAmount);
        billClass.setTotalAmount(totalAmount);

        return billRepository.save(billClass);
    }

    // Get All Bills
    public List<BillClass> getAllBills() {
        return billRepository.findAll();
    }

    // Get Bill By Id
    public BillClass getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    // Update Bill
    public BillClass updateBill(Long id, BillClass billClass) {

        BillClass existingBill = billRepository.findById(id).orElse(null);

        if (existingBill != null) {

            existingBill.setPaymentMethod(billClass.getPaymentMethod());
            existingBill.setPaymentStatus(billClass.getPaymentStatus());
            existingBill.setBillDate(billClass.getBillDate());

            return billRepository.save(existingBill);
        }

        return null;
    }

    // Delete Bill
    public String deleteBill(Long id) {

        if (billRepository.existsById(id)) {

            billRepository.deleteById(id);

            return "Bill Deleted Successfully";
        }

        return "Bill Not Found";
    }
    public List<BillClass> getBillsByPaymentStatus(String paymentStatus) {
        return billRepository.findByPaymentStatus(paymentStatus);
    }
}