package com.salonmanagementsystem.constants;

import lombok.Data;

@Data
public final class AppConstants {

    private AppConstants() {
    }

    // Base Upload Folder
    public static final String UPLOAD_DIR = "uploads/";

    // Employee Images
    public static final String EMPLOYEE_UPLOAD_DIR =
            UPLOAD_DIR + "employees/";

    // Product Images
    public static final String PRODUCT_UPLOAD_DIR =
            UPLOAD_DIR + "products/";
}