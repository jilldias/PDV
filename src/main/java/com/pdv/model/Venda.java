package com.pdv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caixa_id")
    private Caixa caixa;

    @Column(name = "forma_pagamento", length = 50)
    private String formaPagamento;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "data_venda", nullable = false)
    private LocalDateTime dataVenda;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {
    }

    public Venda(Long id) {
        this.id = id;
    }

    public Venda(Long id, Cliente cliente, Funcionario funcionario, Caixa caixa, String formaPagamento, BigDecimal valorTotal, String status, LocalDateTime dataVenda, List<ItemVenda> itens) {
        this.id = id;
        this.cliente = cliente;
        this.funcionario = funcionario;
        this.caixa = caixa;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataVenda = dataVenda;
        this.itens = itens;
    }

    @PrePersist
    public void prePersist() {
        if (dataVenda == null) {
            dataVenda = LocalDateTime.now();
        }
        if (status == null) {
            status = "PROCESSADO";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
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

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
}
