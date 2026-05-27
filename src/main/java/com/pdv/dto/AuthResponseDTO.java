package com.pdv.dto;

import com.pdv.security.Role;

public class AuthResponseDTO {

    private Long funcionarioId;
    private String nome;
    private Role role;
    private String token;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(Long funcionarioId, String nome, Role role, String token) {
        this.funcionarioId = funcionarioId;
        this.nome = nome;
        this.role = role;
        this.token = token;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
