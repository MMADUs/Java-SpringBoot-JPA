package com.domain.restful.handler.auth;

import com.domain.restful.model.entity.AuthEntity;
import com.domain.restful.model.repository.AuthRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class RevokeSession implements LogoutHandler {
    private final AuthRepository authRepository;

    public RevokeSession(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        AuthEntity storedToken = authRepository.findByRefreshToken(token).orElse(null);

        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            authRepository.save(storedToken);
        }
    }
}
