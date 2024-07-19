package com.domain.restful.model.mapper;

import org.mapstruct.Mapper;

import com.domain.restful.handler.types.request.ProductRequest;
import com.domain.restful.model.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductEntity requestToEntity(ProductRequest request);
}
