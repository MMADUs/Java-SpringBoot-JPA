package com.domain.restful.handler.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserImplementation userImplementation;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(final UserImplementation userImplementation, final JwtAuthFilter jwtAuthFilter) {
        this.userImplementation = userImplementation;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    private static final String[] WHITELIST = {
            "/auth/login/**",
            "/auth/register/**",
            "/merchant/**",
            "/product/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RevokeSession revokeSession) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITELIST)
                        .permitAll()
                        .requestMatchers(POST, "/category/**").hasAnyRole(Roles.USER.getRole())
                        .requestMatchers(PUT, "/category/**").hasAnyRole(Roles.ADMIN.getRole())
                        .requestMatchers(DELETE, "/category/**").hasAnyRole(Roles.ADMIN.getRole())
                        .requestMatchers(GET, "/category/**").hasAnyRole(Roles.USER.getRole(), Roles.ADMIN.getRole())
                        .anyRequest()
                        .authenticated()
                )
                .userDetailsService(userImplementation)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.accessDeniedHandler(
                        (request, response, accessDeniedException) -> response.setStatus(403)
                ).authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .logout(l -> l
                        .logoutUrl("/auth/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "DELETE"))
                        .addLogoutHandler(revokeSession)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
