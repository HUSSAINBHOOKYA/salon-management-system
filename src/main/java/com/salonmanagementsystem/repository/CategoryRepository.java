package com.salonmanagementsystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.salonmanagementsystem.entity.Category;


public interface CategoryRepository 
        extends JpaRepository<Category, Long>{

}