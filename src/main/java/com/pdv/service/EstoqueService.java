package com.pdv.service;

import com.pdv.model.MovimentacaoEstoque;
import com.pdv.model.Produto;
import com.pdv.repository.MovimentacaoEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ProdutoService produtoService;

    public EstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository,
                          ProdutoService produtoService) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public MovimentacaoEstoque registrarEntrada(Produto produto, Integer quantidade, String observacao) {
        produto.setEstoque(produto.getEstoque() + quantidade);
        produtoService.atualizarProduto(produto.getId(), produto, produto.getCategoria());

        MovimentacaoEstoque movimento = new MovimentacaoEstoque();
        movimento.setProduto(produto);
        movimento.setQuantidade(quantidade);
        movimento.setTipo("ENTRADA");
        movimento.setObservacao(observacao);
        return movimentacaoEstoqueRepository.save(movimento);
    }

    @Transactional
    public MovimentacaoEstoque registrarSaida(Produto produto, Integer quantidade, String observacao) {
        produto.setEstoque(produto.getEstoque() - quantidade);
        produtoService.atualizarProduto(produto.getId(), produto, produto.getCategoria());

        MovimentacaoEstoque movimento = new MovimentacaoEstoque();
        movimento.setProduto(produto);
        movimento.setQuantidade(quantidade);
        movimento.setTipo("SAÍDA");
        movimento.setObservacao(observacao);
        return movimentacaoEstoqueRepository.save(movimento);
    }

    public List<MovimentacaoEstoque> listarMovimentacoes() {
        return movimentacaoEstoqueRepository.findAll();
    }
}
