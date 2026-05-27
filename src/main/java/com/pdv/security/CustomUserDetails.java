package com.pdv.security;

import com.pdv.model.Funcionario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Funcionario funcionario;

    public CustomUserDetails(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(funcionario.getRole().toAuthority());
    }

    @Override
    public String getPassword() {
        return funcionario.getSenha();
    }

    @Override
    public String getUsername() {
        return funcionario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getFuncionarioId() {
        return funcionario.getId();
    }
}
