package org.hrd.hibernatejpa01.service.implementation;

import lombok.RequiredArgsConstructor;
import org.hrd.hibernatejpa01.exception.BadRequestException;
import org.hrd.hibernatejpa01.exception.NotFoundException;
import org.hrd.hibernatejpa01.model.dto.request.ProductRequest;
import org.hrd.hibernatejpa01.model.dto.response.PaginationResponse;
import org.hrd.hibernatejpa01.model.dto.response.ProductPageResponse;
import org.hrd.hibernatejpa01.model.entity.Product;
import org.hrd.hibernatejpa01.repository.ProductRepository;
import org.hrd.hibernatejpa01.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> createProducts(List<ProductRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BadRequestException("Product list cannot be empty");
        }

        List<Product> products = new ArrayList<>();

        for (ProductRequest request : requests) {
            if (request.getQuantity() <= 0 || request.getQuantity() > 100001) {
                throw new BadRequestException("Invalid quantity for product: " + request.getName());
            } else if (request.getName() == null || request.getName().isEmpty()) {
                throw new BadRequestException("Invalid name for one of the products");
            } else if (request.getPrice() == null || request.getPrice() <= 0) {
                throw new BadRequestException("Invalid price for product: " + request.getName());
            }

            Product product = Product.builder()
                    .name(request.getName().trim())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .build();

            productRepository.createProducts(product); // Save individually
            products.add(product);
        }

        return products;
    }


    @Override
    public Product updateProduct(Long id, ProductRequest request) {
        if (productRepository.getProductById(id) == null) {
            throw new NotFoundException("Product with id: " + id + " not found");
        } else if (request.getName() == null || request.getName().isEmpty()) {
            throw new BadRequestException("Please enter a valid product name");
        } else if (request.getPrice() == null || request.getPrice() <= 0) {
            throw new BadRequestException("Please enter a valid product price or greater than 0");
        } else if (request.getQuantity() <= 0) {
            throw new BadRequestException("Please enter a valid product quantity or greater than 0");
        } else if (request.getQuantity() > 100001) {
            throw new BadRequestException("Please enter a valid product quantity less than 100001");
        }
        return productRepository.updateProduct(id, request);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            throw new NotFoundException("Product with id: " + id + " not found");
        }
        productRepository.deleteProduct(id);
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            throw new NotFoundException("Product with id: " + id + " not found");
        }
        return product;
    }

    @Override
    public ProductPageResponse getAllProducts(Integer page, Integer size) {
        if (page == null || page < 1) page = 1;
        if (size == null || size < 1) size = 10;

        int totalCount = productRepository.countProducts();
        List<Product> products = productRepository.getAllProducts(page, size);

        if (products.isEmpty()) {
            throw new NotFoundException("No products found");
        }

        PaginationResponse pagination = new PaginationResponse(totalCount, page, size);

        return new ProductPageResponse(products, pagination);
    }

    @Override
    public List<Product> findProductBySearchName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NotFoundException("No products found");
        }
        return productRepository.findProductBySearchName(name);
    }

    @Override
    public List<Product> getLowStockProducts(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new NotFoundException("Please enter a valid quantity greater than 0");
        }
        return productRepository.getLowStockProducts(quantity);
    }
}
