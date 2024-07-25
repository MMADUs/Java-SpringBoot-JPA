package com.domain.restful.model.entity;

import com.domain.restful.handler.auth.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Roles role;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    private boolean loggedOut;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can add your own logic here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can add your own logic here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add your own logic here
    }

    @Override
    public boolean isEnabled() {
        return true; // You can add your own logic here
    }
}
