package com.domain.restful.handler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domain.restful.handler.types.request.CategoryRequest;
import com.domain.restful.handler.types.response.http.ApiResponse;
import com.domain.restful.model.entity.CategoryEntity;
import com.domain.restful.model.mapper.CategoryMapper;
import com.domain.restful.usecase.service.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  CategoryService categoryService;

  @Autowired
  CategoryMapper categoryMapper;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryEntity>>> getAllCategory() {
    List<CategoryEntity> categories = categoryService.getAllCategory();
    return ResponseEntity.ok(new ApiResponse<>("Categories retireved successfully", categories, true));
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CategoryEntity>> getCategoryById(@PathVariable Long id) {
    try {
      CategoryEntity category = categoryService.getCategoryById(id);
      return ResponseEntity.ok(new ApiResponse<>("Category found", category, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }
  
  @PostMapping
  public ResponseEntity<ApiResponse<CategoryEntity>> createCategory(@Valid @RequestBody CategoryRequest category) {
    CategoryEntity categoryRequest = categoryMapper.requestToEntity(category);
    CategoryEntity createdCategory = categoryService.createCategory(categoryRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Category created successfully", createdCategory, true));
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<CategoryEntity>> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest category) {
    try {
      CategoryEntity categoryRequest = categoryMapper.requestToEntity(category);
      CategoryEntity updatedCategory = categoryService.updateCategory(id, categoryRequest);
      return ResponseEntity.ok(new ApiResponse<>("Category updated successfully", updatedCategory, false));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
    try {
      categoryService.deleteCategory(id);
      return ResponseEntity.ok(new ApiResponse<>("Categoryt deleted successfully", null, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }
}
