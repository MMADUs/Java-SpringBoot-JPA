package com.domain.restful.handler.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN("ADMIN"),
    USER("USER");

    private final String Permission;
}
