package com.domain.restful.model.mapper;

import com.domain.restful.handler.types.response.json.entity.MerchantResponse;
import com.domain.restful.handler.types.response.json.entity.ProductResponse;
import com.domain.restful.handler.types.response.json.type.Category;
import com.domain.restful.handler.types.response.json.type.Product;
import com.domain.restful.model.entity.ProductEntity;
import org.springframework.stereotype.Component;

import com.domain.restful.handler.types.request.MerchantRequest;
import com.domain.restful.model.entity.MerchantEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MerchantMapper {
  public MerchantEntity requestToEntity(MerchantRequest request) {
    MerchantEntity merchantEntity = new MerchantEntity();

    merchantEntity.setName(request.getName());
    merchantEntity.setLocation(request.getLocation());

    return merchantEntity;
  }

  public List<MerchantResponse> entityListToResponseList(List<MerchantEntity> merchantEntityList) {
    return merchantEntityList.stream()
            .map(this::entityToResponse)
            .collect(Collectors.toList());
  }

  public MerchantResponse entityToResponse(MerchantEntity entity) {
    MerchantResponse merchantResponse = new MerchantResponse();
    merchantResponse.setId(entity.getId());
    merchantResponse.setName(entity.getName());
    merchantResponse.setLocation(entity.getLocation());

    List<Product> products = entity.getProducts().stream()
            .map(this::productForMerchant)
            .toList();
    merchantResponse.setProducts(products);

    return merchantResponse;
  }

  private Product productForMerchant(ProductEntity productEntity) {
    Product product = new Product();
    product.setId(productEntity.getId());
    product.setName(productEntity.getName());
    product.setDescription(productEntity.getDescription());
    product.setPrice(productEntity.getPrice());
    product.setStock(productEntity.getStock());
    product.setAvailable(productEntity.getAvailable());

    Category category = new Category();
    category.setId(productEntity.getCategory().getId());
    category.setName(productEntity.getCategory().getName());
    product.setCategory(category);

    return product;
  }
}
