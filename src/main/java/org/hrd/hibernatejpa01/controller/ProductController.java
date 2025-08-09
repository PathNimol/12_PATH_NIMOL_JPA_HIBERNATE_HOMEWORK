package org.hrd.hibernatejpa01.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.hrd.hibernatejpa01.model.dto.request.ProductRequest;
import org.hrd.hibernatejpa01.model.dto.response.ApiResponse;
import org.hrd.hibernatejpa01.model.dto.response.ProductPageResponse;
import org.hrd.hibernatejpa01.model.entity.Product;
import org.hrd.hibernatejpa01.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Add multiple products")
    public ResponseEntity<ApiResponse<List<Product>>> createProducts(@RequestBody List<ProductRequest> requests) {
        ApiResponse<List<Product>> response = ApiResponse.<List<Product>>builder()
                .message("Products created successfully")
                .payload(productService.createProducts(requests))
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Product by ID")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        ApiResponse<Product> response = ApiResponse.<Product>builder()
                .message("Product ID: " + id + " has been updated successfully")
                .payload(productService.updateProduct(id, request))
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Product by ID")
    public ResponseEntity<ApiResponse<Product>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        ApiResponse<Product> response = ApiResponse.<Product>builder()
                .message("Product ID: " + id + " has been DELETE successfully")
                .payload(null)
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Product by ID")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        ApiResponse<Product> response = ApiResponse.<Product>builder()
                .message("Product ID: " + id + " fetched successfully")
                .payload(productService.getProductById(id))
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all Products (Paginated)")
    public ResponseEntity<ApiResponse<ProductPageResponse>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        ProductPageResponse productPageResponse = productService.getAllProducts(page, size);

        ApiResponse<ProductPageResponse> response = ApiResponse.<ProductPageResponse>builder()
                .message("Products fetched successfully")
                .payload(productPageResponse)
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }


    //Get products by search name
    @GetMapping("/search")
    @Operation(summary = "Search Products by name")
    public ResponseEntity<ApiResponse<List<Product>>> findProductBySearchName(@RequestParam(value = "name", defaultValue = "") String name) {
        ApiResponse<List<Product>> response = ApiResponse.<List<Product>>builder()
                .message("Products matching name '" + name + "' fetched successfully")
                .payload(productService.findProductBySearchName(name))
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }

    //Get low-stock products
    @GetMapping("/low-stock")
    @Operation(summary = "Get low-stock products")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockProducts(@RequestParam(value = "quantity") Integer quantity) {
        ApiResponse<List<Product>> response = ApiResponse.<List<Product>>builder()
                .message("Products with quantity less than " + quantity + " fetched successfully")
                .payload(productService.getLowStockProducts(quantity))
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
