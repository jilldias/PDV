package com.pdv.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ClienteRequestDTO {

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Email(message = "E-mail inválido")
    private String email;

    private String telefone;
    private String endereco;

    public ClienteRequestDTO() {
    }

    public ClienteRequestDTO(String nome, String cpf, String email, String telefone, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
