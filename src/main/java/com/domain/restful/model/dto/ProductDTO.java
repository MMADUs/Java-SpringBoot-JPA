package com.domain.restful.model.dto;

import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.request.ProductRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductDTO {
  public ProductEntity ProductToEntity(ProductRequest request) {
    if (request == null) {
      return null;
    }

    ProductEntity productEntity = new ProductEntity();

    productEntity.setName(request.getName());
    productEntity.setDescription(request.getDescription());
    productEntity.setPrice(request.getPrice());
    productEntity.setStock(request.getStock());
    productEntity.setAvailable(request.getAvailable());

    return productEntity;
  }
}
