package com.domain.restful.handler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domain.restful.handler.types.request.CartRequest;
import com.domain.restful.handler.types.response.ApiResponse;
import com.domain.restful.model.entity.CartEntity;
import com.domain.restful.model.mapper.CartMapper;
import com.domain.restful.usecase.service.CartService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

  @Autowired
  CartService cartService;

  @Autowired
  CartMapper cartMapper;

  @PostMapping("/{cartId}/products/{productId}")
  public ResponseEntity<ApiResponse<CartEntity>> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId) {
    CartEntity addedProducts = cartService.addProductToCart(cartId, productId);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Cart created successfully", addedProducts, true));
  }

  @DeleteMapping("/{cartId}/products/{productId}")
  public ResponseEntity<CartEntity> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
    cartService.removeProductFromCart(cartId, productId);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CartEntity>>> getAllCarts() {
    List<CartEntity> carts = cartService.getAllCart();
    System.out.println(carts);
    return ResponseEntity.ok(new ApiResponse<>("Carts retireved successfully", carts, true));
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CartEntity>> getCartById(@PathVariable Long id) {
    try {
      CartEntity cart = cartService.getCartByID(id);
      return ResponseEntity.ok(new ApiResponse<>("Cart found", cart, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }
  
  @PostMapping
  public ResponseEntity<ApiResponse<CartEntity>> createCart(@Valid @RequestBody CartRequest cart) {
    CartEntity cartRequest = cartMapper.requestToEntity(cart);
    CartEntity createdCart = cartService.createCart(cartRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Cart created successfully", createdCart, true));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<CartEntity>> updateCart(@PathVariable Long id, @RequestBody CartRequest cart) {
    try {
      CartEntity cartRequest = cartMapper.requestToEntity(cart);
      CartEntity updatedCart = cartService.updateCart(id, cartRequest);
      return ResponseEntity.ok(new ApiResponse<>("Cart updated successfully", updatedCart, false));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteCart(@PathVariable Long id) {
    try {
      cartService.deleteCart(id);
      return ResponseEntity.ok(new ApiResponse<>("Cart deleted successfully", null, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }
}
