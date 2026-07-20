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

import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.service.ProductClassService;



@RestController
@RequestMapping("/api/products")
public class ProductClassController {


    @Autowired
    private ProductClassService productClassService;



    // Add Product

    @PostMapping("/add")
    public ProductClass addProduct(
            @RequestBody ProductClass productClass){


        return productClassService.addProduct(productClass);

    }




    // Get All Products

    @GetMapping("/all")
    public List<ProductClass> getAllProducts(){


        return productClassService.getAllProducts();

    }





    // Get Product By Id

    @GetMapping("/{id}")
    public ProductClass getProductById(
            @PathVariable Long id){


        return productClassService.getProductById(id);

    }





    // Update Product

    @PutMapping("/update/{id}")
    public ProductClass updateProduct(
            @PathVariable Long id,
            @RequestBody ProductClass productClass){


        return productClassService.updateProduct(id, productClass);

    }





    // Delete Product

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id){


        return productClassService.deleteProduct(id);

    }
    
    @GetMapping("/search")
    public List<ProductClass> searchProduct(@RequestParam String name) {
        return productClassService.searchProduct(name);
    }

}