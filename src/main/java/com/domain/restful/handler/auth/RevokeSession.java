package com.domain.restful.handler.auth;

import com.domain.restful.model.entity.AuthEntity;
import com.domain.restful.model.repository.AuthRepository;
import com.domain.restful.usecase.util.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Map;
import java.util.Optional;

@Configuration
public class RevokeSession implements LogoutHandler {
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    public RevokeSession(AuthRepository authRepository, JwtService jwtService) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        String refreshToken = getCookieValue(request, "refresh");
        if (refreshToken == null) {
            return;
        }
        System.out.println(refreshToken);

        Map<String, String> payload = jwtService.extractPayload(refreshToken);
        if (payload == null) {
            return;
        }
        System.out.println(payload);

        Optional<AuthEntity> auth = authRepository.findById(Long.parseLong(payload.get("user_id")));
        if (auth.isEmpty()) {
            return;
        }

        AuthEntity userData = auth.get();
        userData.setRefreshToken(null);
        userData.setLoggedOut(true);
        authRepository.save(userData);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
