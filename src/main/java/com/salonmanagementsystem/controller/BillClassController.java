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

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.service.BillClassService;

@RestController
@RequestMapping("/api/bills")
public class BillClassController {

    @Autowired
    private BillClassService billClassService;

    // ============================
    // Add Bill
    // ============================
    @PostMapping("/add")
    public BillClass addBill(@RequestBody BillClass billClass) {

        return billClassService.addBill(billClass);

    }

    // ============================
    // Get All Bills
    // ============================
    @GetMapping("/all")
    public List<BillClass> getAllBills() {

        return billClassService.getAllBills();

    }

    // ============================
    // Get Bill By Id
    // ============================
    @GetMapping("/{id}")
    public BillClass getBillById(
            @PathVariable Long id
    ) {

        return billClassService.getBillById(id);

    }

    // ============================
    // Update Bill
    // ============================
    @PutMapping("/update/{id}")
    public BillClass updateBill(
            @PathVariable Long id,
            @RequestBody BillClass billClass
    ) {

        return billClassService.updateBill(id, billClass);

    }

    // ============================
    // Delete Bill
    // ============================
    @DeleteMapping("/delete/{id}")
    public String deleteBill(
            @PathVariable Long id
    ) {

        return billClassService.deleteBill(id);

    }

    // ============================
    // Search Bills
    // ============================
    @GetMapping("/search")
    public List<BillClass> searchBills(
            @RequestParam String name
    ) {

        return billClassService.searchBills(name);

    }

    // ============================
    // Pagination
    // ============================
    @GetMapping("/pagination")
    public PaginationResponse<BillClass> getBillsPagination(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size

    ) {

        return billClassService.getBillsPagination(page, size);

    }

    // ============================
    // Payment Status Filter
    // ============================
    @GetMapping("/payment/{status}")
    public List<BillClass> getBillsByPaymentStatus(

            @PathVariable PaymentStatus status

    ) {

        return billClassService.getBillsByPaymentStatus(status);

    }

}












//package com.salonmanagementsystem.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.salonmanagementsystem.entity.BillClass;
//import com.salonmanagementsystem.service.BillClassService;
//
//@RestController
//@RequestMapping("/api/bills")
//public class BillClassController {
//
//    @Autowired
//    private BillClassService billClassService;
//
//    // Add Bill
//    @PostMapping("/add")
//    public BillClass addBill(@RequestBody BillClass billClass) {
//
//        return billClassService.addBill(billClass);
//    }
//
//    // Get All Bills
//    @GetMapping("/all")
//    public List<BillClass> getAllBills() {
//
//        return billClassService.getAllBills();
//    }
//
//    // Get Bill By Id
//    @GetMapping("/{id}")
//    public BillClass getBillById(@PathVariable Long id) {
//
//        return billClassService.getBillById(id);
//    }
//
//    // Update Bill
//    @PutMapping("/update/{id}")
//    public BillClass updateBill(@PathVariable Long id,
//                                @RequestBody BillClass billClass) {
//
//        return billClassService.updateBill(id, billClass);
//    }
//
//    // Delete Bill
//    @DeleteMapping("/delete/{id}")
//    public String deleteBill(@PathVariable Long id) {
//
//        return billClassService.deleteBill(id);
//    }
//    
//    @GetMapping("/payment/{status}")
//    public List<BillClass> getBillsByPaymentStatus(
//            @PathVariable String status) {
//
//        return billClassService.getBillsByPaymentStatus(status);
//    }
//
//}