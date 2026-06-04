package com.pdv.model;

public enum VendaStatus {
    PROCESSADO("Processado"),
    CANCELADO("Cancelado"),
    DEVOLVIDO("Devolvido");

    private final String descricao;

    VendaStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
