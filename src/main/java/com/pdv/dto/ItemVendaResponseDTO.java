package com.pdv.dto;

import java.math.BigDecimal;

public class ItemVendaResponseDTO {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private BigDecimal precoUnitario;
    private Integer quantidade;
    private BigDecimal subtotal;

    public ItemVendaResponseDTO() {
    }

    public ItemVendaResponseDTO(Long id, Long produtoId, String produtoNome, BigDecimal precoUnitario, Integer quantidade, BigDecimal subtotal) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
