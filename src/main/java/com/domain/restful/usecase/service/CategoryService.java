package com.domain.restful.usecase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.restful.model.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

import com.domain.restful.model.entity.CategoryEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public List<CategoryEntity> getAllCategory() {
    return categoryRepository.findAll();
  }

  public CategoryEntity getCategoryById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
  }

  public CategoryEntity createCategory(CategoryEntity category) {
    return categoryRepository.save(category);
  }

  public CategoryEntity updateCategory(Long id, CategoryEntity categoryDetails) {
    CategoryEntity category = categoryRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    
    category.setName(categoryDetails.getName());

    return categoryRepository.save(category);
  }

  public void deleteCategory(Long id) {
    if (!categoryRepository.existsById(id)) {
      throw new EntityNotFoundException("Cannot delete. Category not found with id: " + id);
    }
    categoryRepository.deleteById(id);
  }
}
