package com.salonmanagementsystem.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.entity.ProductClass;
import com.salonmanagementsystem.repository.ProductRepository;


@Service
public class ProductClassService {


    @Autowired
    private ProductRepository productRepository;



    // Add Product
    public ProductClass addProduct(ProductClass productClass) {

        return productRepository.save(productClass);
    }



    // Get All Products
    public List<ProductClass> getAllProducts(){

        return productRepository.findAll();
    }




    // Get Product By Id
    public ProductClass getProductById(Long id){

        return productRepository.findById(id)
                .orElse(null);
    }




    // Update Product
    public ProductClass updateProduct(Long id,
                                     ProductClass productClass){


        ProductClass existingProduct =
                productRepository.findById(id)
                .orElse(null);



        if(existingProduct != null){


            existingProduct.setProductName(
                    productClass.getProductName());


            existingProduct.setDescription(
                    productClass.getDescription());


            existingProduct.setCategory(
                    productClass.getCategory());


            existingProduct.setBrand(
                    productClass.getBrand());


            existingProduct.setPrice(
                    productClass.getPrice());


            existingProduct.setStockQuantity(
                    productClass.getStockQuantity());


            existingProduct.setImageUrl(
                    productClass.getImageUrl());


            existingProduct.setStatus(
                    productClass.getStatus());



            return productRepository.save(existingProduct);

        }


        return null;

    }





    // Delete Product
    public String deleteProduct(Long id){


        if(productRepository.existsById(id)){


            productRepository.deleteById(id);


            return "Product Deleted Successfully";

        }


        return "Product Not Found";

    }
    
    public List<ProductClass> searchProduct(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name);
    }
}