package com.pdv.dto;

import com.pdv.model.CaixaStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CaixaResponseDTO {

    private Long id;
    private BigDecimal valorInicial;
    private BigDecimal valorFinal;
    private CaixaStatus status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private Long funcionarioId;
    private String funcionarioNome;

    public CaixaResponseDTO() {
    }

    public CaixaResponseDTO(Long id, BigDecimal valorInicial, BigDecimal valorFinal, CaixaStatus status, LocalDateTime dataAbertura, LocalDateTime dataFechamento, Long funcionarioId, String funcionarioNome) {
        this.id = id;
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
        this.status = status;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
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
}
