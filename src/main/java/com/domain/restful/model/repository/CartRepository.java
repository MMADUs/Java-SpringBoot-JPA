package com.domain.restful.model.repository;

import com.domain.restful.model.entity.CartEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {}
