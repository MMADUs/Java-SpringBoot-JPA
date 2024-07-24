package com.domain.restful.usecase.service;

import com.domain.restful.model.entity.CategoryEntity;
import com.domain.restful.model.entity.MerchantEntity;
import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.repository.CategoryRepository;
import com.domain.restful.model.repository.MerchantRepository;
import com.domain.restful.model.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private MerchantRepository merchantRepository;

  public List<ProductEntity> getAllProducts() {
    return productRepository.findAll();
  }

  public ProductEntity getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
  }

  public ProductEntity createProduct(ProductEntity product) {
    MerchantEntity merchant = product.getMerchant();
    merchant.addProduct(product);

    return productRepository.save(product);
  }

  public ProductEntity updateProduct(Long id, ProductEntity productDetails) {
    ProductEntity product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

    product.setName(productDetails.getName());
    product.setDescription(productDetails.getDescription());
    product.setPrice(productDetails.getPrice());
    product.setStock(productDetails.getStock());
    product.setAvailable(productDetails.getAvailable());

    CategoryEntity categoryEntity = categoryRepository.findById(productDetails.getCategory().getId()).orElse(null);
    product.setCategory(categoryEntity);

    MerchantEntity merchantEntity = merchantRepository.findById(productDetails.getMerchant().getId()).orElse(null);
    product.setMerchant(merchantEntity);

    return productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new EntityNotFoundException("Cannot delete. Product not found with id: " + id);
    }
    productRepository.deleteById(id);
  }
}
