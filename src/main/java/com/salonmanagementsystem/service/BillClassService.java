package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.enums.PaymentStatus;
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

    // ===========================
    // Add Bill
    // ===========================
    public BillClass addBill(BillClass billClass) {

        AppointmentClass appointment = appointmentRepository
                .findById(billClass.getAppointment().getId())
                .orElseThrow(() ->
                        new RuntimeException("Appointment Not Found"));

        ProductOrderClass productOrder = productOrderRepository
                .findById(billClass.getProductOrder().getId())
                .orElseThrow(() ->
                        new RuntimeException("Product Order Not Found"));

        billClass.setAppointment(appointment);
        billClass.setProductOrder(productOrder);

        // Customer Details
        billClass.setCustomerName(
                appointment.getCustomerName());

        billClass.setCustomerPhone(
                appointment.getCustomerPhone());

        billClass.setCustomerEmail(
                appointment.getCustomerEmail());

        // Amounts
        Double serviceAmount =
                appointment.getSalonService().getPrice();

        Double productAmount =
                productOrder.getTotalPrice();

        Double totalAmount =
                serviceAmount + productAmount;

        billClass.setServiceAmount(serviceAmount);
        billClass.setProductAmount(productAmount);
        billClass.setTotalAmount(totalAmount);

        // Save First
        BillClass savedBill = billRepository.save(billClass);

        // Generate Bill Number
        savedBill.setBillNumber(
                "BILL-" + (1000 + savedBill.getId()));

        return billRepository.save(savedBill);
    }

    // ===========================
    // Get All Bills
    // ===========================
    public List<BillClass> getAllBills() {

        return billRepository.findAll();

    }

    // ===========================
    // Get Bill By Id
    // ===========================
    public BillClass getBillById(Long id) {

        return billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill Not Found"));

    }

    // ===========================
    // Update Bill
    // ===========================
    public BillClass updateBill(
            Long id,
            BillClass billClass
    ) {

        BillClass existingBill =
                billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill Not Found"));

        existingBill.setPaymentMethod(
                billClass.getPaymentMethod());

        existingBill.setPaymentStatus(
                billClass.getPaymentStatus());

        existingBill.setBillDate(
                billClass.getBillDate());

        return billRepository.save(existingBill);

    }

    // ===========================
    // Delete Bill
    // ===========================
    public String deleteBill(Long id) {

        BillClass bill =
                billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill Not Found"));

        billRepository.delete(bill);

        return "Bill Deleted Successfully";

    }

    // ===========================
    // Search Bills
    // ===========================
    public List<BillClass> searchBills(String customerName) {

        Pageable pageable =
                PageRequest.of(0, Integer.MAX_VALUE);

        return billRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName,
                        pageable)
                .getContent();

    }

    // ===========================
    // Pagination
    // ===========================
    public PaginationResponse<BillClass> getBillsPagination(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<BillClass> billPage =
                billRepository.findAll(pageable);

        return new PaginationResponse<>(

                billPage.getContent(),

                billPage.getNumber(),

                billPage.getTotalPages(),

                billPage.getTotalElements(),

                billPage.getSize()

        );

    }

    // ===========================
    // Payment Status Filter
    // ===========================
    public List<BillClass> getBillsByPaymentStatus(
            PaymentStatus paymentStatus
    ) {

        Pageable pageable =
                PageRequest.of(0, Integer.MAX_VALUE);

        return billRepository
                .findByPaymentStatus(
                        paymentStatus,
                        pageable)
                .getContent();

    }

}























//package com.salonmanagementsystem.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.salonmanagementsystem.entity.AppointmentClass;
//import com.salonmanagementsystem.entity.BillClass;
//import com.salonmanagementsystem.entity.ProductOrderClass;
//import com.salonmanagementsystem.repository.AppointmentRepository;
//import com.salonmanagementsystem.repository.BillRepository;
//import com.salonmanagementsystem.repository.ProductOrderRepository;
//
//@Service
//public class BillClassService {
//
//    @Autowired
//    private BillRepository billRepository;
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private ProductOrderRepository productOrderRepository;
//
//    // Add Bill
//    public BillClass addBill(BillClass billClass) {
//
//        AppointmentClass appointment = appointmentRepository
//                .findById(billClass.getAppointment().getId())
//                .orElseThrow(() -> new RuntimeException("Appointment Not Found"));
//
//        ProductOrderClass productOrder = productOrderRepository
//                .findById(billClass.getProductOrder().getId())
//                .orElseThrow(() -> new RuntimeException("Product Order Not Found"));
//
//        billClass.setAppointment(appointment);
//        billClass.setProductOrder(productOrder);
//
//        // Service Amount
//        Double serviceAmount = appointment.getService().getPrice();
//
//        // Product Amount
//        Double productAmount = productOrder.getTotalPrice();
//
//        // Total Amount
//        Double totalAmount = serviceAmount + productAmount;
//
//        billClass.setCustomerName(appointment.getCustomerName());
//        billClass.setServiceAmount(serviceAmount);
//        billClass.setProductAmount(productAmount);
//        billClass.setTotalAmount(totalAmount);
//
//        return billRepository.save(billClass);
//    }
//
//    // Get All Bills
//    public List<BillClass> getAllBills() {
//        return billRepository.findAll();
//    }
//
//    // Get Bill By Id
//    public BillClass getBillById(Long id) {
//        return billRepository.findById(id).orElse(null);
//    }
//
//    // Update Bill
//    public BillClass updateBill(Long id, BillClass billClass) {
//
//        BillClass existingBill = billRepository.findById(id).orElse(null);
//
//        if (existingBill != null) {
//
//            existingBill.setPaymentMethod(billClass.getPaymentMethod());
//            existingBill.setPaymentStatus(billClass.getPaymentStatus());
//            existingBill.setBillDate(billClass.getBillDate());
//
//            return billRepository.save(existingBill);
//        }
//
//        return null;
//    }
//
//    // Delete Bill
//    public String deleteBill(Long id) {
//
//        if (billRepository.existsById(id)) {
//
//            billRepository.deleteById(id);
//
//            return "Bill Deleted Successfully";
//        }
//
//        return "Bill Not Found";
//    }
//    public List<BillClass> getBillsByPaymentStatus(String paymentStatus) {
//        return billRepository.findByPaymentStatus(paymentStatus);
//    }
//}