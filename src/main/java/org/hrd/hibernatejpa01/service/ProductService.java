package org.hrd.hibernatejpa01.service;

import org.hrd.hibernatejpa01.model.dto.request.ProductRequest;
import org.hrd.hibernatejpa01.model.dto.response.ProductPageResponse;
import org.hrd.hibernatejpa01.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> createProducts(List<ProductRequest> requests);

    Product updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    Product getProductById(Long id);

    ProductPageResponse getAllProducts(Integer page, Integer size);

    List<Product> findProductBySearchName(String name);

    List<Product> getLowStockProducts(Integer quantity);

}
