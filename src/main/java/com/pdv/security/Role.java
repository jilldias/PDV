package com.pdv.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ADMIN,
    OPERADOR;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + name());
    }
}
