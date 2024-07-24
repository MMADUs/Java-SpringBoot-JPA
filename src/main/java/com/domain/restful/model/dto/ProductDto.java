package com.domain.restful.model.dto;

import org.springframework.stereotype.Component;

import com.domain.restful.model.entity.CategoryEntity;
import com.domain.restful.model.entity.ProductEntity;

@Component
public class ProductDto {
  public ProductEntity dataToObject(ProductEntity request) {
    ProductEntity productEntity = new ProductEntity();

    productEntity.setName(request.getName());
    productEntity.setDescription(request.getDescription());
    productEntity.setPrice(request.getPrice());
    productEntity.setStock(request.getStock());
    productEntity.setAvailable(request.getAvailable());

    if (request.getCategory() != null) {
      CategoryEntity category = new CategoryEntity();

      category.setId(request.getCategory().getId());
      category.setName(request.getCategory().getName());

      productEntity.setCategory(category);
    }

    return productEntity;
  }
}
