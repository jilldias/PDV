package com.pdv.javafx.util;

import com.pdv.model.ItemVenda;
import com.pdv.model.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Optional;

public class CarrinhoCompras {

    private final ObservableList<ItemVenda> itens = FXCollections.observableArrayList();

    public void adicionarProduto(Produto produto, Integer quantidade) {
        if (produto == null || quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto e quantidade inválidos");
        }

        Optional<ItemVenda> itemExistente = itens.stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemVenda item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        } else {
            ItemVenda novoItem = new ItemVenda();
            novoItem.setProduto(produto);
            novoItem.setQuantidade(quantidade);
            novoItem.setPrecoUnitario(produto.getPreco());
            novoItem.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
            itens.add(novoItem);
        }
    }

    public void removerProduto(Long produtoId) {
        itens.removeIf(item -> item.getProduto().getId().equals(produtoId));
    }

    public void atualizarQuantidade(Long produtoId, Integer novaQuantidade) {
        if (novaQuantidade <= 0) {
            removerProduto(produtoId);
            return;
        }

        itens.stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantidade(novaQuantidade);
                    item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(novaQuantidade)));
                });
    }

    public void limpar() {
        itens.clear();
    }

    public BigDecimal obterTotal() {
        return itens.stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int obterQuantidadeTotalItens() {
        return itens.stream()
                .mapToInt(ItemVenda::getQuantidade)
                .sum();
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public ObservableList<ItemVenda> getItens() {
        return itens;
    }
}
