package com.salonmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.entity.ProductOrderClass;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.enums.ProductStatus;
import com.salonmanagementsystem.repository.BillRepository;
import com.salonmanagementsystem.repository.ProductOrderRepository;
import com.salonmanagementsystem.repository.ProductRepository;

@Service
public class ProductOrderClassService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillClassService billClassService;

    @Autowired
    private BillRepository billRepository;

    // =========================================
    // ADD PRODUCT ORDER
    // CUSTOMER ORDER
    // =========================================

    @Transactional
    public ProductOrderClass addProductOrder(
            ProductOrderClass productOrderClass) {

        ProductClass product = productRepository
                .findById(productOrderClass.getProduct().getId())
                .orElseThrow(
                        () -> new RuntimeException("Product Not Found")
                );

        // =========================================
        // CHECK STOCK
        // =========================================

        if (product.getStockQuantity()
                < productOrderClass.getQuantity()) {

            throw new RuntimeException(
                    "Insufficient Stock"
            );
        }

        // =========================================
        // CALCULATE TOTAL PRICE
        // =========================================

        Double totalPrice =
                product.getPrice()
                        * productOrderClass.getQuantity();

        productOrderClass.setTotalPrice(totalPrice);

        // =========================================
        // REDUCE STOCK
        // =========================================

        product.setStockQuantity(
                product.getStockQuantity()
                        - productOrderClass.getQuantity()
        );

        // =========================================
        // UPDATE PRODUCT STATUS
        // =========================================

        if (product.getStockQuantity() == 0) {

            product.setStatus(
                    ProductStatus.OUT_OF_STOCK
            );

        } else {

            product.setStatus(
                    ProductStatus.AVAILABLE
            );
        }

        // =========================================
        // SAVE PRODUCT
        // =========================================

        productRepository.save(product);

        // =========================================
        // SET PRODUCT
        // =========================================

        productOrderClass.setProduct(product);

        // =========================================
        // SAVE PRODUCT ORDER
        // =========================================

        ProductOrderClass savedOrder =
                productOrderRepository.save(
                        productOrderClass
                );

        // =========================================
        // AUTOMATIC BILL GENERATION
        // =========================================

        BillClass bill = new BillClass();

        bill.setProductOrder(savedOrder);

        bill.setPaymentStatus(
                savedOrder.getPaymentStatus() != null
                        ? savedOrder.getPaymentStatus()
                        : PaymentStatus.PENDING
        );

        bill.setPaymentMethod("CASH");

        billClassService.addBill(bill);

        // =========================================
        // RETURN SAVED ORDER
        // =========================================

        return savedOrder;
    }

    // =========================================
    // GET ALL ORDERS
    // =========================================

    public List<ProductOrderClass> getAllOrders() {

        return productOrderRepository.findAll();
    }

    // =========================================
    // GET MY ORDERS
    // =========================================

    public List<ProductOrderClass> getMyOrders(
            String customerEmail) {

        return productOrderRepository
                .findByCustomerEmailOrderByIdDesc(
                        customerEmail
                );
    }

    // =========================================
    // GET ORDER BY ID
    // =========================================

    public ProductOrderClass getOrderById(Long id) {

        return productOrderRepository
                .findById(id)
                .orElse(null);
    }

    // =========================================
    // UPDATE ORDER
    // =========================================

    @Transactional
    public ProductOrderClass updateOrder(
            Long id,
            ProductOrderClass productOrderClass) {

        ProductOrderClass existingOrder =
                productOrderRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Order Not Found"
                                )
                        );

        ProductClass oldProduct =
                existingOrder.getProduct();

        ProductClass newProduct =
                productRepository
                        .findById(
                                productOrderClass
                                        .getProduct()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product Not Found"
                                )
                        );

        // =========================================
        // RESTORE OLD STOCK
        // =========================================

        oldProduct.setStockQuantity(
                oldProduct.getStockQuantity()
                        + existingOrder.getQuantity()
        );

        if (oldProduct.getStockQuantity() > 0) {

            oldProduct.setStatus(
                    ProductStatus.AVAILABLE
            );

        } else {

            oldProduct.setStatus(
                    ProductStatus.OUT_OF_STOCK
            );
        }

        productRepository.save(oldProduct);

        // =========================================
        // CHECK NEW STOCK
        // =========================================

        if (newProduct.getStockQuantity()
                < productOrderClass.getQuantity()) {

            throw new RuntimeException(
                    "Insufficient Stock"
            );
        }

        // =========================================
        // REDUCE NEW STOCK
        // =========================================

        newProduct.setStockQuantity(
                newProduct.getStockQuantity()
                        - productOrderClass.getQuantity()
        );

        if (newProduct.getStockQuantity() == 0) {

            newProduct.setStatus(
                    ProductStatus.OUT_OF_STOCK
            );

        } else {

            newProduct.setStatus(
                    ProductStatus.AVAILABLE
            );
        }

        productRepository.save(newProduct);

        // =========================================
        // UPDATE ORDER DETAILS
        // =========================================

        existingOrder.setCustomerName(
                productOrderClass.getCustomerName()
        );

        existingOrder.setCustomerPhone(
                productOrderClass.getCustomerPhone()
        );

        existingOrder.setCustomerEmail(
                productOrderClass.getCustomerEmail()
        );

        existingOrder.setQuantity(
                productOrderClass.getQuantity()
        );

        // =========================================
        // UPDATE PAYMENT STATUS
        // =========================================

        PaymentStatus updatedPaymentStatus =
                productOrderClass.getPaymentStatus();

        if (updatedPaymentStatus == null) {

            updatedPaymentStatus =
                    existingOrder.getPaymentStatus() != null
                            ? existingOrder.getPaymentStatus()
                            : PaymentStatus.PENDING;
        }

        existingOrder.setPaymentStatus(
                updatedPaymentStatus
        );

        existingOrder.setOrderDate(
                productOrderClass.getOrderDate()
        );

        existingOrder.setProduct(
                newProduct
        );

        // =========================================
        // RECALCULATE TOTAL PRICE
        // =========================================

        existingOrder.setTotalPrice(
                newProduct.getPrice()
                        * productOrderClass.getQuantity()
        );

        // =========================================
        // SAVE UPDATED ORDER
        // =========================================

        ProductOrderClass savedOrder =
                productOrderRepository.save(
                        existingOrder
                );

        // =========================================
        // UPDATE RELATED BILL
        // PRODUCT ORDER → BILL
        // =========================================

        BillClass relatedBill =
                billRepository
                        .findByProductOrderId(savedOrder.getId())
                        .orElse(null);

        if (relatedBill != null) {

            relatedBill.setPaymentStatus(
                    updatedPaymentStatus
            );

            billRepository.save(relatedBill);
        }

        // =========================================
        // RETURN UPDATED ORDER
        // =========================================

        return savedOrder;
    }

    // =========================================
    // DELETE ORDER
    // =========================================

    @Transactional
    public String deleteOrder(Long id) {

        ProductOrderClass order =
                productOrderRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Order Not Found"
                                )
                        );

        ProductClass product =
                order.getProduct();

        // =========================================
        // RESTORE STOCK
        // =========================================

        product.setStockQuantity(
                product.getStockQuantity()
                        + order.getQuantity()
        );

        // =========================================
        // UPDATE PRODUCT STATUS
        // =========================================

        if (product.getStockQuantity() > 0) {

            product.setStatus(
                    ProductStatus.AVAILABLE
            );
        }

        productRepository.save(product);

        // =========================================
        // DELETE ORDER
        // =========================================

        productOrderRepository.delete(order);

        return "Order Deleted Successfully";
    }

    // =========================================
    // SEARCH ORDERS
    // =========================================

    public List<ProductOrderClass> searchOrders(
            String name) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE
                );

        return productOrderRepository
                .findByCustomerNameContainingIgnoreCase(
                        name,
                        pageable
                )
                .getContent();
    }

    // =========================================
    // PAGINATION
    // =========================================

    public PaginationResponse<ProductOrderClass>
    getOrdersPagination(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<ProductOrderClass> orderPage =
                productOrderRepository
                        .findAll(pageable);

        return new PaginationResponse<>(
                orderPage.getContent(),
                orderPage.getNumber(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements(),
                orderPage.getSize()
        );
    }
}