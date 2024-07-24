package com.domain.restful.usecase.service;

import com.domain.restful.handler.types.request.LoginRequest;
import com.domain.restful.model.entity.AuthEntity;
import com.domain.restful.model.repository.AuthRepository;
import com.domain.restful.usecase.util.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AuthEntity> getAllUser() {
        return authRepository.findAll();
    }

    public AuthEntity getUserById(Long id) {
        return authRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auth user not found with id: " + id));
    }

    public AuthEntity createUser(AuthEntity authEntity) {
        AuthEntity newUser = new AuthEntity();

        String hashedPassword = passwordEncoder.encode(authEntity.getPassword());

        newUser.setUsername(authEntity.getUsername());
        newUser.setEmail(authEntity.getEmail());
        newUser.setRole(authEntity.getRole());
        newUser.setPassword(hashedPassword);
        newUser.setRefreshToken(null);
        newUser.setLoggedOut(true);

        return authRepository.save(newUser);
    }

    public AuthEntity updateUser(Long id, AuthEntity authEntity) {
        AuthEntity currentUser = authRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auth user not found with id:" + id));

        currentUser.setUsername(authEntity.getUsername());
        currentUser.setEmail(authEntity.getEmail());
        currentUser.setRole(authEntity.getRole());
        currentUser.setPassword(passwordEncoder.encode(authEntity.getPassword()));

        return authRepository.save(authEntity);
    }

    public void deleteUser(Long id) {
        if (!authRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. User not found with id: " + id);
        }
        authRepository.deleteById(id);
    }

    public Map<String, String> login(LoginRequest loginRequest) {
        AuthEntity user = authRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            Map<String, String> payload = new HashMap<>();
            payload.put("user_id", user.getId().toString());

            String accessToken = jwtService.generateAccessToken(user.getUsername(), payload);
            String refreshToken = jwtService.generateRefreshToken(user.getUsername(), payload);

            user.setRefreshToken(refreshToken);
            user.setLoggedOut(false);
            authRepository.save(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);

            return tokens;
        }

        throw new EntityNotFoundException("User not found");
    }
}
