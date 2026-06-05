package com.pdv.javafx.state;

import com.pdv.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoFormState {

    private Produto produtoEmEdicao;

    public Produto getProdutoEmEdicao() {
        return produtoEmEdicao;
    }

    public void editar(Produto produto) {
        this.produtoEmEdicao = produto;
    }

    public void novo() {
        this.produtoEmEdicao = null;
    }

    public boolean isEdicao() {
        return produtoEmEdicao != null && produtoEmEdicao.getId() != null;
    }
}
