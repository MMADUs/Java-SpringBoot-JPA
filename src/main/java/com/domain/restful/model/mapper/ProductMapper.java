package com.domain.restful.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.domain.restful.handler.types.request.ProductRequest;
import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.entity.CategoryEntity;
import com.domain.restful.model.entity.MerchantEntity;
import com.domain.restful.model.repository.CategoryRepository;
import com.domain.restful.model.repository.MerchantRepository;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  MerchantRepository merchantRepository;

  public ProductEntity requestToEntity(ProductRequest request) {
    ProductEntity productEntity = new ProductEntity();
    productEntity.setName(request.getName());
    productEntity.setDescription(request.getDescription());
    productEntity.setPrice(request.getPrice());
    productEntity.setStock(request.getStock());
    productEntity.setAvailable(request.getAvailable());

    CategoryEntity categoryEntity = categoryRepository.findById(request.getCategory_id()).orElse(null);
    productEntity.setCategory(categoryEntity);

    MerchantEntity merchantEntity = merchantRepository.findById(request.getMerchant_id()).orElse(null);
    productEntity.setMerchant(merchantEntity);

    return productEntity;
  }
}