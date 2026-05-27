package com.pdv.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaRequestDTO {

    @NotBlank(message = "Nome da categoria é obrigatório")
    private String nome;
    private String descricao;

    public CategoriaRequestDTO() {
    }

    public CategoriaRequestDTO(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
