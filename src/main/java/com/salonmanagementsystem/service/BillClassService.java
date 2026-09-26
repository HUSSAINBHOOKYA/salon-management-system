package com.salonmanagementsystem.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.AdminBillResponse;
import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.BillRepository;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.SalonRepository;

@Service
public class BillClassService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private SalonRepository salonRepository;


    // =========================================================
    // ADD BILL
    // =========================================================

    @Transactional
    public BillClass addBill(BillClass billClass) {

        // =====================================================
        // APPOINTMENT
        // =====================================================

        AppointmentClass appointment = null;

        if (billClass.getAppointment() != null
                && billClass.getAppointment().getId() != null) {

            appointment = appointmentRepository
                    .findById(
                            billClass
                                    .getAppointment()
                                    .getId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Appointment Not Found"
                            )
                    );

            billClass.setAppointment(appointment);
        }


        // =====================================================
        // PRODUCT ORDER
        // =====================================================

        ProductOrderClass productOrder = null;

        if (billClass.getProductOrder() != null
                && billClass.getProductOrder().getId() != null) {

            productOrder = productOrderRepository
                    .findById(
                            billClass
                                    .getProductOrder()
                                    .getId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product Order Not Found"
                            )
                    );

            billClass.setProductOrder(productOrder);
        }


        // =====================================================
        // VALIDATE BILL SOURCE
        // =====================================================

        if (appointment == null && productOrder == null) {

            throw new RuntimeException(
                    "Appointment or Product Order is required"
            );
        }


        // =====================================================
        // CUSTOMER DETAILS
        // =====================================================

        if (appointment != null) {

            billClass.setCustomerName(
                    appointment.getCustomerName()
            );

            billClass.setCustomerPhone(
                    appointment.getCustomerPhone()
            );

            billClass.setCustomerEmail(
                    appointment.getCustomerEmail()
            );

        } else if (productOrder != null) {

            billClass.setCustomerName(
                    productOrder.getCustomerName()
            );

            billClass.setCustomerPhone(
                    productOrder.getCustomerPhone()
            );

            billClass.setCustomerEmail(
                    productOrder.getCustomerEmail()
            );
        }


        // =====================================================
        // SERVICE AMOUNT
        // =====================================================

        Double serviceAmount = 0.0;

        if (appointment != null
                && appointment.getSalonService() != null
                && appointment.getSalonService().getPrice() != null) {

            serviceAmount =
                    appointment
                            .getSalonService()
                            .getPrice();
        }


        // =====================================================
        // PRODUCT AMOUNT
        // =====================================================

        Double productAmount = 0.0;

        if (productOrder != null
                && productOrder.getTotalPrice() != null) {

            productAmount =
                    productOrder.getTotalPrice();
        }


        // =====================================================
        // TOTAL AMOUNT
        // =====================================================

        Double totalAmount =
                serviceAmount + productAmount;

        billClass.setServiceAmount(
                serviceAmount
        );

        billClass.setProductAmount(
                productAmount
        );

        billClass.setTotalAmount(
                totalAmount
        );


        // =====================================================
        // DEFAULT PAYMENT STATUS
        // =====================================================

        if (billClass.getPaymentStatus() == null) {

            billClass.setPaymentStatus(
                    PaymentStatus.PENDING
            );
        }


        // =====================================================
        // DEFAULT PAYMENT METHOD
        // =====================================================

        if (billClass.getPaymentMethod() == null
                || billClass.getPaymentMethod().isBlank()) {

            billClass.setPaymentMethod("CASH");
        }


        // =====================================================
        // DEFAULT BILL DATE
        // =====================================================

        if (billClass.getBillDate() == null) {

            billClass.setBillDate(
                    LocalDate.now()
            );
        }


        // =====================================================
        // SAVE BILL
        // =====================================================

        BillClass savedBill =
                billRepository.save(
                        billClass
                );


        // =====================================================
        // GENERATE BILL NUMBER
        // =====================================================

        savedBill.setBillNumber(
                "BILL-" +
                (1000 + savedBill.getId())
        );


        // =====================================================
        // SAVE UPDATED BILL
        // =====================================================

        return billRepository.save(
                savedBill
        );
    }


    // =========================================================
    // ADMIN - GET ALL BILLS
    // =========================================================

    @Transactional(readOnly = true)
    public List<AdminBillResponse> getAllBills() {

        return billRepository
                .findAll()
                .stream()
                .map(this::mapToAdminBillResponse)
                .toList();
    }


    // =========================================================
    // CUSTOMER - GET MY BILLS
    // =========================================================

    @Transactional(readOnly = true)
    public List<AdminBillResponse> getMyBills(
            String customerEmail
    ) {

        return billRepository
                .findByCustomerEmailIgnoreCase(
                        customerEmail
                )
                .stream()
                .map(this::mapToAdminBillResponse)
                .toList();
    }


    // =========================================================
    // PARTNER - GET MY SALON BILLS
    // =========================================================

    @Transactional(readOnly = true)
    public List<AdminBillResponse> getPartnerBills(
            String partnerEmail
    ) {

        // =====================================================
        // FIND PARTNER SALON
        // =====================================================

        Salon partnerSalon =
                salonRepository
                        .findByEmail(partnerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Partner salon not found"
                                )
                        );

        Long salonId =
                partnerSalon.getId();


        // =====================================================
        // FIND BILLS BELONGING TO PARTNER SALON
        // =====================================================

        return billRepository
                .findAll()
                .stream()
                .filter(bill ->
                        belongsToSalon(
                                bill,
                                salonId
                        )
                )
                .map(this::mapToAdminBillResponse)
                .toList();
    }


    // =========================================================
    // CHECK BILL → SALON
    // =========================================================

    private boolean belongsToSalon(
            BillClass bill,
            Long salonId
    ) {

        // =====================================================
        // CHECK APPOINTMENT SALON
        // =====================================================

        if (bill.getAppointment() != null
                && bill.getAppointment().getSalon() != null) {

            Long appointmentSalonId =
                    bill.getAppointment()
                            .getSalon()
                            .getId();

            if (salonId.equals(
                    appointmentSalonId
            )) {

                return true;
            }
        }


        // =====================================================
        // CHECK PRODUCT SALON
        // =====================================================

        if (bill.getProductOrder() != null
                && bill.getProductOrder().getProduct() != null
                && bill.getProductOrder()
                        .getProduct()
                        .getSalon() != null) {

            Long productSalonId =
                    bill.getProductOrder()
                            .getProduct()
                            .getSalon()
                            .getId();

            if (salonId.equals(
                    productSalonId
            )) {

                return true;
            }
        }


        return false;
    }


    // =========================================================
    // GET BILL BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public AdminBillResponse getBillById(
            Long id
    ) {

        BillClass bill =
                billRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bill Not Found"
                                )
                        );

        return mapToAdminBillResponse(
                bill
        );
    }


    // =========================================================
    // UPDATE BILL
    // =========================================================

    @Transactional
    public BillClass updateBill(
            Long id,
            BillClass billClass
    ) {

        // =====================================================
        // FIND EXISTING BILL
        // =====================================================

        BillClass existingBill =
                billRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bill Not Found"
                                )
                        );


        // =====================================================
        // PAYMENT METHOD
        // =====================================================

        if (billClass.getPaymentMethod() != null
                && !billClass.getPaymentMethod().isBlank()) {

            existingBill.setPaymentMethod(
                    billClass.getPaymentMethod()
            );
        }


        // =====================================================
        // PAYMENT STATUS
        // =====================================================

        if (billClass.getPaymentStatus() != null) {

            PaymentStatus newPaymentStatus =
                    billClass.getPaymentStatus();


            // =================================================
            // UPDATE BILL PAYMENT STATUS
            // =================================================

            existingBill.setPaymentStatus(
                    newPaymentStatus
            );


            // =================================================
            // SYNC PRODUCT ORDER PAYMENT STATUS
            // =================================================

            if (existingBill.getProductOrder() != null) {

                ProductOrderClass productOrder =
                        existingBill.getProductOrder();

                productOrder.setPaymentStatus(
                        newPaymentStatus
                );

                productOrderRepository.save(
                        productOrder
                );
            }


            // =================================================
            // SYNC APPOINTMENT PAYMENT STATUS
            // =================================================

            if (existingBill.getAppointment() != null) {

                AppointmentClass appointment =
                        existingBill.getAppointment();

                appointment.setPaymentStatus(
                        newPaymentStatus
                );

                appointmentRepository.save(
                        appointment
                );
            }
        }


        // =====================================================
        // BILL DATE
        // =====================================================

        if (billClass.getBillDate() != null) {

            existingBill.setBillDate(
                    billClass.getBillDate()
            );
        }


        // =====================================================
        // SAVE BILL
        // =====================================================

        return billRepository.save(
                existingBill
        );
    }


    // =========================================================
    // DELETE BILL
    // =========================================================

    @Transactional
    public String deleteBill(
            Long id
    ) {

        BillClass bill =
                billRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bill Not Found"
                                )
                        );

        billRepository.delete(
                bill
        );

        return "Bill Deleted Successfully";
    }


    // =========================================================
    // SEARCH BILLS
    // =========================================================

    public List<BillClass> searchBills(
            String customerName
    ) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE
                );

        return billRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName,
                        pageable
                )
                .getContent();
    }


    // =========================================================
    // PAGINATION
    // =========================================================

    public PaginationResponse<BillClass> getBillsPagination(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size
                );

        Page<BillClass> billPage =
                billRepository.findAll(
                        pageable
                );

        return new PaginationResponse<>(
                billPage.getContent(),
                billPage.getNumber(),
                billPage.getTotalPages(),
                billPage.getTotalElements(),
                billPage.getSize()
        );
    }


    // =========================================================
    // PAYMENT STATUS FILTER
    // =========================================================

    public List<BillClass> getBillsByPaymentStatus(
            PaymentStatus paymentStatus
    ) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE
                );

        return billRepository
                .findByPaymentStatus(
                        paymentStatus,
                        pageable
                )
                .getContent();
    }


    // =========================================================
    // MAP BILL → DTO
    // =========================================================

    private AdminBillResponse mapToAdminBillResponse(
            BillClass bill
    ) {

        AdminBillResponse response =
                new AdminBillResponse();


        // =====================================================
        // BILL
        // =====================================================

        response.setId(
                bill.getId()
        );

        response.setBillNumber(
                bill.getBillNumber()
        );

        response.setCustomerName(
                bill.getCustomerName()
        );

        response.setCustomerPhone(
                bill.getCustomerPhone()
        );

        response.setCustomerEmail(
                bill.getCustomerEmail()
        );

        response.setServiceAmount(
                bill.getServiceAmount()
        );

        response.setProductAmount(
                bill.getProductAmount()
        );

        response.setTotalAmount(
                bill.getTotalAmount()
        );

        response.setPaymentMethod(
                bill.getPaymentMethod()
        );

        response.setPaymentStatus(
                bill.getPaymentStatus()
        );

        response.setBillDate(
                bill.getBillDate()
        );


        // =====================================================
        // APPOINTMENT
        // =====================================================

        AppointmentClass appointment =
                bill.getAppointment();

        if (appointment != null) {

            response.setAppointmentId(
                    appointment.getId()
            );

            response.setAppointmentDate(
                    appointment.getAppointmentDate()
            );

            response.setAppointmentTime(
                    appointment.getAppointmentTime()
            );


            // =================================================
            // SALON
            // =================================================

            if (appointment.getSalon() != null) {

                response.setSalonId(
                        appointment
                                .getSalon()
                                .getId()
                );

                response.setSalonName(
                        appointment
                                .getSalon()
                                .getSalonName()
                );
            }


            // =================================================
            // SERVICE
            // =================================================

            if (appointment.getSalonService() != null) {

                response.setServiceName(
                        appointment
                                .getSalonService()
                                .getServiceName()
                );
            }


            // =================================================
            // EMPLOYEE
            // =================================================

            if (appointment.getEmployee() != null) {

                response.setEmployeeName(
                        appointment
                                .getEmployee()
                                .getEmployeeName()
                );
            }
        }


        // =====================================================
        // PRODUCT ORDER
        // =====================================================

        ProductOrderClass productOrder =
                bill.getProductOrder();

        if (productOrder != null) {

            response.setProductOrderId(
                    productOrder.getId()
            );

            response.setProductQuantity(
                    productOrder.getQuantity()
            );


            ProductClass product =
                    productOrder.getProduct();

            if (product != null) {

                response.setProductName(
                        product.getProductName()
                );

                response.setProductImage(
                        product.getImageUrl()
                );


                // =============================================
                // FALLBACK SALON FROM PRODUCT
                // =============================================

                if (response.getSalonName() == null
                        && product.getSalon() != null) {

                    response.setSalonId(
                            product
                                    .getSalon()
                                    .getId()
                    );

                    response.setSalonName(
                            product
                                    .getSalon()
                                    .getSalonName()
                    );
                }
            }
        }


        return response;
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