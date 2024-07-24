package com.domain.restful.model.repository;

import com.domain.restful.model.entity.AuthEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByUsername(String username);
    Optional<AuthEntity> findByRefreshToken(String email);
}
