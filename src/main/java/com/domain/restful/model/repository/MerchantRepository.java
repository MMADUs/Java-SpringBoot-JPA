package com.domain.restful.model.repository;

import com.domain.restful.model.entity.MerchantEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {}