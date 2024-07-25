package com.domain.restful.handler.auth;

import com.domain.restful.model.entity.AuthEntity;
import com.domain.restful.model.repository.AuthRepository;
import com.domain.restful.usecase.util.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserImplementation userImplementation;
    private final AuthRepository authRepository;

    public JwtAuthFilter(final JwtService jwtService, final UserImplementation userImplementation, final AuthRepository authRepository) {
        this.jwtService = jwtService;
        this.userImplementation = userImplementation;
        this.authRepository = authRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = getCookieValue(request, "access");
        String refreshToken = getCookieValue(request, "refresh");

        if (accessToken == null || refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = getSubject(accessToken, refreshToken);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userImplementation.loadUserByUsername(username);

            Map<String, String> accessTokenPayload = jwtService.extractPayload(refreshToken);
            if (accessTokenPayload == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.validateToken(accessToken, userDetails)) {
                grantAccess(request, userDetails, accessTokenPayload.get("user_id"));
                filterChain.doFilter(request, response);
                return;
            }

            Map<String, String> refreshTokenPayload = jwtService.extractPayload(refreshToken);
            if (refreshTokenPayload == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<AuthEntity> userLookup = authRepository.findById(Long.parseLong(refreshTokenPayload.get("user_id")));
            if (userLookup.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            AuthEntity userData = userLookup.get();
            if (userData.getRefreshToken() == null || !userData.getRefreshToken().equals(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.validateToken(userData.getRefreshToken(), userDetails)) {
                grantAccess(request, userDetails, refreshTokenPayload.get("user_id"));

                Map<String, String> newPayload = new HashMap<>();
                newPayload.put("user_id", userData.getId().toString());

                String newAccessToken = jwtService.generateAccessToken(userData.getUsername(), newPayload);

                sendCookie(response, "access", newAccessToken);

                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void grantAccess (HttpServletRequest request, UserDetails userDetails, String id) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.setAttribute("user_id", id);
    }

    private String getSubject(String accessToken, String refreshToken) {
        String usernameFromAccessToken = jwtService.extractSubject(accessToken);
        if (usernameFromAccessToken != null) {
            return usernameFromAccessToken;
        }
        return jwtService.extractSubject(refreshToken);
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

    private void sendCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
