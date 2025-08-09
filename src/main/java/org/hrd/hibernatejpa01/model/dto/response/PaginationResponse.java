package org.hrd.hibernatejpa01.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PaginationResponse {
    private int totalElements;   // total count of items
    private int currentPage;     // current page number
    private int pageSize;        // size of each page
    private int totalPages;      // total number of pages

    public PaginationResponse(int totalElements, int currentPage, int pageSize) {
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = (totalElements / pageSize) + ((totalElements % pageSize) > 0 ? 1 : 0);
    }
}

