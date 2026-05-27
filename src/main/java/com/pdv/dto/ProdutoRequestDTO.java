package com.pdv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Código de barras é obrigatório")
    private String codigoBarras;

    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço deve ser maior ou igual a zero")
    private BigDecimal preco;

    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0, message = "Estoque deve ser maior ou igual a zero")
    private Integer estoque;

    private Boolean ativo;
    private Long categoriaId;

    public ProdutoRequestDTO() {
    }

    public ProdutoRequestDTO(String nome, String codigoBarras, BigDecimal preco, Integer estoque, Boolean ativo, Long categoriaId) {
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
        this.categoriaId = categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
