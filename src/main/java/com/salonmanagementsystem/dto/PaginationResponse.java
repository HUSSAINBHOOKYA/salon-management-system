package com.salonmanagementsystem.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PaginationResponse<T> {


    private List<T> content;

    private int currentPage;

    private int totalPages;

    private long totalElements;

    private int pageSize;


}