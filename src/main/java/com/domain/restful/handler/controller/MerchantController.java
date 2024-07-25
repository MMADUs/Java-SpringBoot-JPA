package com.domain.restful.handler.controller;

import java.util.List;

import com.domain.restful.handler.types.response.json.entity.MerchantResponse;
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

import com.domain.restful.handler.types.request.MerchantRequest;
import com.domain.restful.handler.types.response.http.ApiResponse;
import com.domain.restful.model.entity.MerchantEntity;
import com.domain.restful.model.mapper.MerchantMapper;
import com.domain.restful.usecase.service.MerchantService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
  private final MerchantService merchantService;
  private final MerchantMapper merchantMapper;

  public MerchantController(MerchantService merchantService, MerchantMapper merchantMapper) {
    this.merchantService = merchantService;
    this.merchantMapper = merchantMapper;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<MerchantResponse>>> getAllMerchant() {
    List<MerchantEntity> merchants = merchantService.getAllMerchant();
    List<MerchantResponse> merchantResponseList = merchantMapper.entityListToResponseList(merchants);
    return ResponseEntity.ok(new ApiResponse<>("Merchants retireved successfully", merchantResponseList, true));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<MerchantResponse>> getMerchantById(@PathVariable Long id) {
    try {
      MerchantEntity merchant = merchantService.getMerchantById(id);
      MerchantResponse merchantResponse = merchantMapper.entityToResponse(merchant);
      return ResponseEntity.ok(new ApiResponse<>("Merchant found", merchantResponse, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse<MerchantEntity>> createMerchant(@Valid @RequestBody MerchantRequest merchant) {
    MerchantEntity merchantRequest = merchantMapper.requestToEntity(merchant);
    MerchantEntity createdMerchant = merchantService.createMerchant(merchantRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new ApiResponse<>("Merchant created successfully", createdMerchant, true));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<MerchantEntity>> updateMerchant(@PathVariable Long id,
      @RequestBody MerchantRequest merchant) {
    try {
      MerchantEntity merchantRequest = merchantMapper.requestToEntity(merchant);
      MerchantEntity updatedMerchant = merchantService.updateMerchant(id, merchantRequest);
      return ResponseEntity.ok(new ApiResponse<>("Merchant updated successfully", updatedMerchant, false));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
    try {
      merchantService.deleteMerchant(id);
      return ResponseEntity.ok(new ApiResponse<>("Merchant deleted successfully", null, true));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ApiResponse<>(e.getMessage(), null, false));
    }
  }
}
