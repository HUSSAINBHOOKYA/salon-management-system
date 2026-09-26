package com.salonmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.service.ProductOrderClassService;

@RestController
@RequestMapping("/api/product-orders")
public class ProductOrderClassController {

    @Autowired
    private ProductOrderClassService productOrderClassService;


    // =========================================
    // ADD ORDER
    // =========================================

    @PostMapping("/add")
    public ProductOrderClass addOrder(
            @RequestBody ProductOrderClass productOrderClass) {

        return productOrderClassService
                .addProductOrder(productOrderClass);
    }


    // =========================================
    // GET ALL ORDERS
    // =========================================

    @GetMapping("/all")
    public List<ProductOrderClass> getAllOrders() {

        return productOrderClassService
                .getAllOrders();
    }


    // =========================================
    // GET MY ORDERS
    // =========================================

    @GetMapping("/my")
    public List<ProductOrderClass> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        return productOrderClassService
                .getMyOrders(
                        userDetails.getUsername()
                );
    }


    // =========================================
    // GET ORDER BY ID
    // =========================================

    @GetMapping("/{id}")
    public ProductOrderClass getOrderById(
            @PathVariable Long id) {

        return productOrderClassService
                .getOrderById(id);
    }


    // =========================================
    // UPDATE ORDER
    // =========================================

    @PutMapping("/update/{id}")
    public ProductOrderClass updateOrder(
            @PathVariable Long id,
            @RequestBody ProductOrderClass productOrderClass) {

        return productOrderClassService
                .updateOrder(
                        id,
                        productOrderClass
                );
    }


    // =========================================
    // DELETE ORDER
    // =========================================

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(
            @PathVariable Long id) {

        return productOrderClassService
                .deleteOrder(id);
    }


    // =========================================
    // SEARCH ORDERS
    // =========================================

    @GetMapping("/search")
    public List<ProductOrderClass> searchOrders(
            @RequestParam String name) {

        return productOrderClassService
                .searchOrders(name);
    }


    // =========================================
    // PAGINATION
    // =========================================

    @GetMapping("/pagination")
    public PaginationResponse<ProductOrderClass>
    getOrdersPagination(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return productOrderClassService
                .getOrdersPagination(
                        page,
                        size
                );
    }

}