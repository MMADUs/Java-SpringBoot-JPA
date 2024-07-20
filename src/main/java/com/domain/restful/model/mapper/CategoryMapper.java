package com.domain.restful.model.mapper;

import com.domain.restful.handler.types.request.CategoryRequest;
import com.domain.restful.model.entity.CategoryEntity;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
  public CategoryEntity requestToEntity(CategoryRequest request) {
    CategoryEntity categoryEntity = new CategoryEntity();

    categoryEntity.setName(request.getName());

    return categoryEntity;
  }
}
