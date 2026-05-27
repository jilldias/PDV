package com.pdv.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "caixas")
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_inicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorInicial;

    @Column(name = "valor_final", precision = 15, scale = 2)
    private BigDecimal valorFinal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CaixaStatus status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    public Caixa() {
    }

    public Caixa(Long id) {
        this.id = id;
    }

    public Caixa(Long id, BigDecimal valorInicial, BigDecimal valorFinal, CaixaStatus status, LocalDateTime dataAbertura, LocalDateTime dataFechamento, Funcionario funcionario) {
        this.id = id;
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
        this.status = status;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.funcionario = funcionario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }

    public CaixaStatus getStatus() {
        return status;
    }

    public void setStatus(CaixaStatus status) {
        this.status = status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
