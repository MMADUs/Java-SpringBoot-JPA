package com.domain.restful.model.mapper;

import com.domain.restful.handler.types.response.json.entity.ProductResponse;
import com.domain.restful.handler.types.response.json.type.Category;
import com.domain.restful.handler.types.response.json.type.Merchant;

import com.domain.restful.handler.types.request.ProductRequest;
import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.entity.CategoryEntity;
import com.domain.restful.model.entity.MerchantEntity;
import com.domain.restful.model.repository.CategoryRepository;
import com.domain.restful.model.repository.MerchantRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
  private final CategoryRepository categoryRepository;
  private final MerchantRepository merchantRepository;

  public ProductMapper(CategoryRepository categoryRepository, MerchantRepository merchantRepository) {
    this.categoryRepository = categoryRepository;
    this.merchantRepository = merchantRepository;
  }

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

  public List<ProductResponse> entityListToResponseList(List<ProductEntity> productEntities) {
    return productEntities.stream()
            .map(this::entityToResponse)
            .collect(Collectors.toList());
  }

  public ProductResponse entityToResponse(ProductEntity productEntity) {
    ProductResponse productResponse = new ProductResponse();
    productResponse.setId(productEntity.getId());
    productResponse.setName(productEntity.getName());
    productResponse.setDescription(productEntity.getDescription());
    productResponse.setPrice(productEntity.getPrice());
    productResponse.setStock(productEntity.getStock());
    productResponse.setAvailable(productEntity.getAvailable());

    Category category = new Category();
    category.setId(productEntity.getCategory().getId());
    category.setName(productEntity.getCategory().getName());
    productResponse.setCategory(category);

    Merchant merchant = new Merchant();
    merchant.setId(productEntity.getMerchant().getId());
    merchant.setName(productEntity.getMerchant().getName());
    merchant.setLocation(productEntity.getMerchant().getLocation());
    productResponse.setMerchant(merchant);

    return productResponse;
  }
}