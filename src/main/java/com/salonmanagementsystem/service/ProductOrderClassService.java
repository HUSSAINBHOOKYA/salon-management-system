package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.enums.ProductStatus;
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
            throw new RuntimeException("Insufficient Stock");
        }

        // Calculate Total Price
        Double totalPrice =
                product.getPrice() * productOrderClass.getQuantity();

        productOrderClass.setTotalPrice(totalPrice);

        // Reduce Stock
        product.setStockQuantity(
                product.getStockQuantity() - productOrderClass.getQuantity());

        // Update Product Status
        if (product.getStockQuantity() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else {
            product.setStatus(ProductStatus.AVAILABLE);
        }

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

//    // Update Order
//    public ProductOrderClass updateOrder(Long id,
//            ProductOrderClass productOrderClass) {
//
//        ProductOrderClass existingOrder =
//                productOrderRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Order Not Found"));
//
//        ProductClass product = productRepository
//                .findById(productOrderClass.getProduct().getId())
//                .orElseThrow(() -> new RuntimeException("Product Not Found"));
//
//        existingOrder.setCustomerName(productOrderClass.getCustomerName());
//        existingOrder.setCustomerPhone(productOrderClass.getCustomerPhone());
//        existingOrder.setCustomerEmail(productOrderClass.getCustomerEmail());
//        existingOrder.setQuantity(productOrderClass.getQuantity());
//        existingOrder.setPaymentStatus(productOrderClass.getPaymentStatus());
//        existingOrder.setOrderDate(productOrderClass.getOrderDate());
//        existingOrder.setProduct(product);
//
//        Double totalPrice =
//                product.getPrice() * productOrderClass.getQuantity();
//
//        existingOrder.setTotalPrice(totalPrice);
//
//        return productOrderRepository.save(existingOrder);
//    }
    
 // Update Order
    public ProductOrderClass updateOrder(Long id,
            ProductOrderClass productOrderClass) {

        ProductOrderClass existingOrder = productOrderRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        ProductClass oldProduct = existingOrder.getProduct();

        ProductClass newProduct = productRepository
                .findById(productOrderClass.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        // Restore old stock
        oldProduct.setStockQuantity(
                oldProduct.getStockQuantity() + existingOrder.getQuantity());

        if (oldProduct.getStockQuantity() > 0) {
            oldProduct.setStatus(ProductStatus.AVAILABLE);
        } else {
            oldProduct.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        productRepository.save(oldProduct);

        // Check new stock
        if (newProduct.getStockQuantity() < productOrderClass.getQuantity()) {
            throw new RuntimeException("Insufficient Stock");
        }

        // Reduce new stock
        newProduct.setStockQuantity(
                newProduct.getStockQuantity() - productOrderClass.getQuantity());

        if (newProduct.getStockQuantity() == 0) {
            newProduct.setStatus(ProductStatus.OUT_OF_STOCK);
        } else {
            newProduct.setStatus(ProductStatus.AVAILABLE);
        }

        productRepository.save(newProduct);

        // Update order details
        existingOrder.setCustomerName(productOrderClass.getCustomerName());
        existingOrder.setCustomerPhone(productOrderClass.getCustomerPhone());
        existingOrder.setCustomerEmail(productOrderClass.getCustomerEmail());
        existingOrder.setQuantity(productOrderClass.getQuantity());
        existingOrder.setPaymentStatus(productOrderClass.getPaymentStatus());
        existingOrder.setOrderDate(productOrderClass.getOrderDate());
        existingOrder.setProduct(newProduct);

        existingOrder.setTotalPrice(
                newProduct.getPrice() * productOrderClass.getQuantity());

        return productOrderRepository.save(existingOrder);
    }

    // Delete Order
    public String deleteOrder(Long id) {

        ProductOrderClass order = productOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        ProductClass product = order.getProduct();

        // Restore Stock
        product.setStockQuantity(
                product.getStockQuantity() + order.getQuantity());

        if (product.getStockQuantity() > 0) {
            product.setStatus(ProductStatus.AVAILABLE);
        }

        productRepository.save(product);

        productOrderRepository.delete(order);

        return "Order Deleted Successfully";
    }
 // Search Orders
    public List<ProductOrderClass> searchOrders(String name) {

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        return productOrderRepository
                .findByCustomerNameContainingIgnoreCase(name, pageable)
                .getContent();

    }

    // Pagination
    public PaginationResponse<ProductOrderClass> getOrdersPagination(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductOrderClass> orderPage =
                productOrderRepository.findAll(pageable);

        return new PaginationResponse<>(

                orderPage.getContent(),

                orderPage.getNumber(),

                orderPage.getTotalPages(),

                orderPage.getTotalElements(),

                orderPage.getSize()

        );

    }
}