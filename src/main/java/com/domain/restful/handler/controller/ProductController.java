package com.domain.restful.handler.controller;

import com.domain.restful.handler.types.request.ProductRequest;
import com.domain.restful.handler.types.response.http.ApiResponse;
import com.domain.restful.handler.types.response.json.entity.ProductResponse;
import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.mapper.ProductMapper;
import com.domain.restful.usecase.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
  private final ProductService productService;
  private final ProductMapper productMapper;

  ProductController(ProductService productService, ProductMapper productMapper) {
    this.productService = productService;
    this.productMapper = productMapper;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(HttpServletRequest request) {
    String userId = (String) request.getAttribute("user_id");
    System.out.println("this is user id: " + userId);
    List<ProductEntity> products = productService.getAllProducts();
    List<ProductResponse> productResponses = productMapper.entityListToResponseList(products);
    return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully", productResponses, true));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
    try {
      ProductEntity product = productService.getProductById(id);
      ProductResponse productResponse = productMapper.entityToResponse(product);
      return ResponseEntity.ok(new ApiResponse<>("Product found", productResponse, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductEntity>> createProduct(@Valid @RequestBody ProductRequest product) {
    ProductEntity productRequest = productMapper.requestToEntity(product);
    ProductEntity createdProduct = productService.createProduct(productRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Product created successfully", createdProduct, true));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductEntity>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest product) {
    try {
      ProductEntity productDetails = productMapper.requestToEntity(product);
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
