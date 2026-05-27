package com.pdv.dto;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class VendaRequestDTO {

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    private Long funcionarioId;
    private Long caixaId;
    private String formaPagamento;
    private List<ItemVendaRequestDTO> itens = new ArrayList<>();

    public VendaRequestDTO() {
    }

    public VendaRequestDTO(Long clienteId, Long funcionarioId, Long caixaId, String formaPagamento, List<ItemVendaRequestDTO> itens) {
        this.clienteId = clienteId;
        this.funcionarioId = funcionarioId;
        this.caixaId = caixaId;
        this.formaPagamento = formaPagamento;
        this.itens = itens;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getCaixaId() {
        return caixaId;
    }

    public void setCaixaId(Long caixaId) {
        this.caixaId = caixaId;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public List<ItemVendaRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaRequestDTO> itens) {
        this.itens = itens;
    }
}
