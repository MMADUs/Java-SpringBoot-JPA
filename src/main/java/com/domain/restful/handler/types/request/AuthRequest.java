package com.domain.restful.handler.types.request;

import com.domain.restful.handler.auth.Roles;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Size(min = 5, max = 30, message = "Email must be between 5 and 30 characters")
    private String email;

    private Roles role;
}
