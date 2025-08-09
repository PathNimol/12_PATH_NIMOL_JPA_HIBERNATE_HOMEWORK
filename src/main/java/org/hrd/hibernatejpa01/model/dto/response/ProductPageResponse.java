package org.hrd.hibernatejpa01.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hrd.hibernatejpa01.model.entity.Product;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductPageResponse {
    private List<Product> products;
    private PaginationResponse pagination;
}
