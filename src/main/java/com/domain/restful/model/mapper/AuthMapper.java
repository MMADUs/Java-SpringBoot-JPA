package com.domain.restful.model.mapper;

import com.domain.restful.handler.types.request.AuthRequest;
import com.domain.restful.handler.types.request.LoginRequest;
import com.domain.restful.model.entity.AuthEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
  public AuthEntity requestToEntity(AuthRequest authRequest) {
      AuthEntity authEntity = new AuthEntity();

      authEntity.setUsername(authRequest.getUsername());
      authEntity.setPassword(authRequest.getPassword());
      authEntity.setEmail(authRequest.getEmail());
      authEntity.setRole(authRequest.getRole());

      return authEntity;
  }
}
