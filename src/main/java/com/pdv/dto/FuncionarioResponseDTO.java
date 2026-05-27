package com.pdv.dto;

import com.pdv.security.Role;

public class FuncionarioResponseDTO {

    private Long id;
    private String nome;
    private String cargo;
    private String login;
    private Role role;
    private String email;
    private String telefone;

    public FuncionarioResponseDTO() {
    }

    public FuncionarioResponseDTO(Long id, String nome, String cargo, String login, Role role, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.login = login;
        this.role = role;
        this.email = email;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
