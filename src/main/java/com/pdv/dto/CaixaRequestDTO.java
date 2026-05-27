package com.pdv.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CaixaRequestDTO {

    @NotNull(message = "Valor inicial é obrigatório")
    private BigDecimal valorInicial;

    @NotNull(message = "Funcionário é obrigatório")
    private Long funcionarioId;

    private String status;

    public CaixaRequestDTO() {
    }

    public CaixaRequestDTO(BigDecimal valorInicial, Long funcionarioId, String status) {
        this.valorInicial = valorInicial;
        this.funcionarioId = funcionarioId;
        this.status = status;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
