package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.ProductRepository;

@Service
public class ProductOrderClassService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add Product Order
    public ProductOrderClass addProductOrder(ProductOrderClass productOrderClass) {

        ProductClass product = productRepository
                .findById(productOrderClass.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        // Check Stock
        if (product.getStockQuantity() < productOrderClass.getQuantity()) {
            throw new RuntimeException("Product Out Of Stock");
        }

        // Calculate Total Price
        Double totalPrice =
                product.getPrice() * productOrderClass.getQuantity();

        productOrderClass.setTotalPrice(totalPrice);

        // Reduce Stock
        product.setStockQuantity(
                product.getStockQuantity() - productOrderClass.getQuantity());

        productRepository.save(product);

        productOrderClass.setProduct(product);

        return productOrderRepository.save(productOrderClass);
    }

    // Get All Orders
    public List<ProductOrderClass> getAllOrders() {

        return productOrderRepository.findAll();
    }

    // Get Order By Id
    public ProductOrderClass getOrderById(Long id) {

        return productOrderRepository.findById(id).orElse(null);
    }

    // Update Order
    public ProductOrderClass updateOrder(Long id,
                                         ProductOrderClass productOrderClass) {

        ProductOrderClass existingOrder =
                productOrderRepository.findById(id).orElse(null);

        if (existingOrder != null) {

            ProductClass product = productRepository
                    .findById(productOrderClass.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            existingOrder.setCustomerName(productOrderClass.getCustomerName());
            existingOrder.setCustomerPhone(productOrderClass.getCustomerPhone());
            existingOrder.setCustomerEmail(productOrderClass.getCustomerEmail());
            existingOrder.setQuantity(productOrderClass.getQuantity());
            existingOrder.setPaymentStatus(productOrderClass.getPaymentStatus());
            existingOrder.setOrderDate(productOrderClass.getOrderDate());
            existingOrder.setProduct(product);

            Double total =
                    product.getPrice() * productOrderClass.getQuantity();

            existingOrder.setTotalPrice(total);

            return productOrderRepository.save(existingOrder);
        }

        return null;
    }

    // Delete Order
    public String deleteOrder(Long id) {

        if (productOrderRepository.existsById(id)) {

            productOrderRepository.deleteById(id);

            return "Order Deleted Successfully";
        }

        return "Order Not Found";
    }
    
    public List<ProductOrderClass> getOrdersByCustomer(String customerName) {
        return productOrderRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

}