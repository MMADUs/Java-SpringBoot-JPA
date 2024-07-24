package com.domain.restful.model.mapper;

import org.springframework.stereotype.Component;

import com.domain.restful.handler.types.request.MerchantRequest;
import com.domain.restful.model.entity.MerchantEntity;

@Component
public class MerchantMapper {
  public MerchantEntity requestToEntity(MerchantRequest request) {
    MerchantEntity merchantEntity = new MerchantEntity();

    merchantEntity.setName(request.getName());
    merchantEntity.setLocation(request.getLocation());

    return merchantEntity;
  }
}
