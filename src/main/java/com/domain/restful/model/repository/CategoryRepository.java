package com.domain.restful.model.repository;

import com.domain.restful.model.entity.CategoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {}