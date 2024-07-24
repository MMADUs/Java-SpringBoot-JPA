package com.domain.restful.model.mapper;

import com.domain.restful.handler.types.request.CartRequest;
import com.domain.restful.model.entity.CartEntity;

import org.springframework.stereotype.Component;

@Component
public class CartMapper {
  public CartEntity requestToEntity(CartRequest request) {
    CartEntity cartEntity = new CartEntity();

    cartEntity.setName(request.getName());

    return cartEntity;
  }
}
