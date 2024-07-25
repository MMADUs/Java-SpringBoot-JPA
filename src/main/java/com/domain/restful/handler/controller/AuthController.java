package com.domain.restful.handler.controller;

import com.domain.restful.handler.types.request.AuthRequest;
import com.domain.restful.handler.types.request.LoginRequest;
import com.domain.restful.handler.types.response.ApiResponse;
import com.domain.restful.model.entity.AuthEntity;
import com.domain.restful.model.mapper.AuthMapper;
import com.domain.restful.usecase.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final AuthService authService;
    final AuthMapper authMapper;

    @Autowired
    public AuthController(AuthService authService, AuthMapper authMapper) {
        this.authService = authService;
        this.authMapper = authMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthEntity>>> getAllUser() {
        List<AuthEntity> authEntities = authService.getAllUser();
        return ResponseEntity.ok(new ApiResponse<>("Users retrived successfully", authEntities, true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthEntity>> getUserById(@PathVariable Long id) {
        AuthEntity authEntity = authService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>("User retrived successfully", authEntity, true));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthEntity>> createUser(@Valid @RequestBody AuthRequest request) {
        AuthEntity userRequest = authMapper.requestToEntity(request);
        AuthEntity createdUser = authService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("User created successfully", createdUser, true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthEntity>> updateUser(@Valid @PathVariable Long id, @RequestBody AuthRequest request) {
        try {
            AuthEntity updateRequest = authMapper.requestToEntity(request);
            AuthEntity updatedCart = authService.updateUser(id, updateRequest);
            return ResponseEntity.ok(new ApiResponse<>("User updated successfully", updatedCart, false));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null, false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthEntity>> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", null, true));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(e.getMessage(), null, false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest authRequest, HttpServletResponse response) {
        Map<String, String> tokens = authService.login(authRequest);

        Cookie access = new Cookie("access", tokens.get("access_token"));
        access.setHttpOnly(true);
        access.setSecure(true);
        access.setPath("/");

        Cookie refresh = new Cookie("refresh", tokens.get("refresh_token"));
        refresh.setHttpOnly(true);
        refresh.setSecure(true);
        refresh.setPath("/");

        System.out.println(tokens.get("access_token"));

        response.addCookie(access);
        response.addCookie(refresh);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Login successfully", null, true));
    }
}
