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
import org.springframework.web.bind.annotation.RestController;

import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.service.ProductOrderClassService;

@RestController
@RequestMapping("/api/product-orders")
public class ProductOrderClassController {

    @Autowired
    private ProductOrderClassService productOrderClassService;

    // Add Order
    @PostMapping("/add")
    public ProductOrderClass addOrder(
            @RequestBody ProductOrderClass productOrderClass) {

        return productOrderClassService.addProductOrder(productOrderClass);
    }

    // Get All Orders
    @GetMapping("/all")
    public List<ProductOrderClass> getAllOrders() {

        return productOrderClassService.getAllOrders();
    }

    // Get Order By Id
    @GetMapping("/{id}")
    public ProductOrderClass getOrderById(@PathVariable Long id) {

        return productOrderClassService.getOrderById(id);
    }

    // Update Order
    @PutMapping("/update/{id}")
    public ProductOrderClass updateOrder(
            @PathVariable Long id,
            @RequestBody ProductOrderClass productOrderClass) {

        return productOrderClassService.updateOrder(id, productOrderClass);
    }

    // Delete Order
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {

        return productOrderClassService.deleteOrder(id);
    }
    
    @GetMapping("/customer/{name}")
    public List<ProductOrderClass> getOrdersByCustomer(
            @PathVariable String name) {

        return productOrderClassService.getOrdersByCustomer(name);
    }

}