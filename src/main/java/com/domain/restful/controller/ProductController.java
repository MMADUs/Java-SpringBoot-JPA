package com.domain.restful.controller;

import com.domain.restful.model.dto.ProductDTO;
import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.response.ApiResponse;
import com.domain.restful.service.ProductService;
import com.domain.restful.model.request.ProductRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductDTO productDTO;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductEntity>>> getAllProducts() {
    List<ProductEntity> products = productService.getAllProducts();
    return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully", products, true));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductEntity>> getProductById(@PathVariable Long id) {
    try {
      ProductEntity product = productService.getProductById(id);
      return ResponseEntity.ok(new ApiResponse<>("Product found", product, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductEntity>> createProduct(@Valid @RequestBody ProductRequest product) {
    ProductEntity productRequest = productDTO.ProductToEntity(product);
    ProductEntity createdProduct = productService.createProduct(productRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Product created successfully", createdProduct, true));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductEntity>> updateProduct(@PathVariable Long id, @RequestBody ProductEntity productDetails) {
    try {
      ProductEntity updatedProduct = productService.updateProduct(id, productDetails);
      return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", updatedProduct, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully", null, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }
}
