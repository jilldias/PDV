package com.pdv.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String codigoBarras;
    private BigDecimal preco;
    private Integer estoque;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private Long categoriaId;
    private String categoriaNome;

    public ProdutoResponseDTO() {
    }

    public ProdutoResponseDTO(Long id, String nome, String codigoBarras, BigDecimal preco, Integer estoque, Boolean ativo, LocalDateTime dataCadastro, Long categoriaId, String categoriaNome) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.preco = preco;
        this.estoque = estoque;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
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

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
}
