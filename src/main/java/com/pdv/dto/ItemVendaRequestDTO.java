package com.pdv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemVendaRequestDTO {

    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;

    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    private Integer quantidade;

    public ItemVendaRequestDTO() {
    }

    public ItemVendaRequestDTO(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
