package com.pdv.validation;

import com.pdv.exception.BusinessException;
import com.pdv.model.Produto;

public final class DomainValidator {

    private DomainValidator() {
    }

    public static void validarProdutoParaVenda(Produto produto, Integer quantidade) {
        if (produto == null) {
            throw new BusinessException("Produto não encontrado para venda");
        }
        if (!Boolean.TRUE.equals(produto.getAtivo())) {
            throw new BusinessException("Produto inativo não pode ser vendido");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new BusinessException("Quantidade de venda deve ser maior que zero");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < quantidade) {
            throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNome());
        }
    }
}
