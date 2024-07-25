package com.domain.restful.handler.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Roles {
    ADMIN("ADMIN"),
    USER("USER");

    @Getter
    private final String role;
}
