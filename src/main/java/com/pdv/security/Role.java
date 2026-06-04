package com.pdv.security;

public enum Role {
    ADMIN("Administrador"),
    GERENTE("Gerente"),
    VENDEDOR("Vendedor"),
    OPERADOR_CAIXA("Operador de Caixa");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
