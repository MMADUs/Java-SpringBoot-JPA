package com.domain.restful.usecase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.restful.model.entity.CartEntity;
import com.domain.restful.model.entity.ProductEntity;
import com.domain.restful.model.repository.CartRepository;
import com.domain.restful.model.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductRepository productRepository;

  @Transactional
  public CartEntity addProductToCart(Long cartId, Long productId) {
    CartEntity cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new RuntimeException("Cart not found"));
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    cart.getProducts().add(product);
    return cartRepository.save(cart);
  }

  @Transactional
  public void removeProductFromCart(Long cartId, Long productId) {
    CartEntity cart = cartRepository.findById(cartId)
        .orElseThrow(() -> new RuntimeException("Cart not found"));
    ProductEntity product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Product not found"));

    cart.getProducts().remove(product);
    cartRepository.save(cart);
  }

  public List<CartEntity> getAllCart() {
    return cartRepository.findAll();
  }

  public CartEntity getCartByID(Long id) {
    return cartRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + id));
  }

  public CartEntity createCart(CartEntity cart) {
    return cartRepository.save(cart);
  }

    public CartEntity updateCart(Long id, CartEntity cartDetails) {
    CartEntity category = cartRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + id));
    
    category.setName(cartDetails.getName());

    return cartRepository.save(category);
  }

  public void deleteCart(Long id) {
    if (!cartRepository.existsById(id)) {
      throw new EntityNotFoundException("Cannot delete. Category not found with id: " + id);
    }
    cartRepository.deleteById(id);
  }
}
