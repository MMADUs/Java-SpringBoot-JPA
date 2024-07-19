package com.domain.restful.model.repository;

import com.domain.restful.model.entity.ProductEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
  @EntityGraph(attributePaths = { "category" })
  List<ProductEntity> findAllWithCategory();
}
