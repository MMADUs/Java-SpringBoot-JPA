package com.domain.restful.model.repository;

import com.domain.restful.model.entity.CategoryEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
  @EntityGraph(attributePaths = { "products" })
  List<CategoryEntity> findAllWithProducts();
}