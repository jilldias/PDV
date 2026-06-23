package com.pdv.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaResponseDTO {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Long funcionarioId;
    private String funcionarioNome;
    private Long caixaId;
    private String formaPagamento;
    private BigDecimal total;
    private BigDecimal valorPago;
    private BigDecimal troco;
    private String status;
    private LocalDateTime dataVenda;
    private List<ItemVendaResponseDTO> itens = new ArrayList<>();

    public VendaResponseDTO() {
    }

    public VendaResponseDTO(Long id, Long clienteId, String clienteNome, Long funcionarioId, String funcionarioNome, Long caixaId, String formaPagamento, BigDecimal total, BigDecimal valorPago, BigDecimal troco, String status, LocalDateTime dataVenda, List<ItemVendaResponseDTO> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.caixaId = caixaId;
        this.formaPagamento = formaPagamento;
        this.total = total;
        this.valorPago = valorPago;
        this.troco = troco;
        this.status = status;
        this.dataVenda = dataVenda;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ItemVendaResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaResponseDTO> itens) {
        this.itens = itens;
    }
}
