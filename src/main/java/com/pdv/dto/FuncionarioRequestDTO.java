package com.pdv.dto;

import com.pdv.security.Role;
import jakarta.validation.constraints.NotBlank;

public class FuncionarioRequestDTO {

    @NotBlank(message = "Nome do funcionário é obrigatório")
    private String nome;
    private String cargo;
    private String login;
    private String senha;
    private Role role;
    private String email;
    private String telefone;

    public FuncionarioRequestDTO() {
    }

    public FuncionarioRequestDTO(String nome, String cargo, String login, String senha, Role role, String email, String telefone) {
        this.nome = nome;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.role = role;
        this.email = email;
        this.telefone = telefone;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
