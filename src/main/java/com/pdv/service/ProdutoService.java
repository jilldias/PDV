package com.pdv.service;

import com.pdv.exception.BusinessException;
import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Categoria;
import com.pdv.model.Produto;
import com.pdv.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto criarProduto(Produto produto, Categoria categoria) {
        validarProduto(produto, categoria);
        produto.setCategoria(categoria);
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Produto> buscarPorCodigoBarras(String codigoBarras) {
        return produtoRepository.findByCodigoBarras(codigoBarras);
    }

    @Transactional
    public Produto atualizarProduto(Long id, Produto dadosAtualizados, Categoria categoria) {
        validarProduto(dadosAtualizados, categoria);

        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        produtoExistente.setNome(dadosAtualizados.getNome());
        produtoExistente.setCodigoBarras(dadosAtualizados.getCodigoBarras());
        produtoExistente.setPreco(dadosAtualizados.getPreco());
        produtoExistente.setEstoque(dadosAtualizados.getEstoque());
        produtoExistente.setAtivo(dadosAtualizados.getAtivo());
        produtoExistente.setCategoria(categoria);

        return produtoRepository.save(produtoExistente);
    }

    public void removerProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto", "id", id);
        }
        produtoRepository.deleteById(id);
    }

    public void diminuirEstoque(Produto produto, Integer quantidade) {
        if (produto == null) {
            throw new BusinessException("Produto não encontrado");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero");
        }
        if (produto.getEstoque() < quantidade) {
            throw new BusinessException("Estoque insuficiente: " + produto.getNome());
        }
        produto.setEstoque(produto.getEstoque() - quantidade);
        produtoRepository.save(produto);
    }

    private void validarProduto(Produto produto, Categoria categoria) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        if (produto.getCodigoBarras() == null || produto.getCodigoBarras().trim().isEmpty()) {
            throw new IllegalArgumentException("Código de barras é obrigatório");
        }
        if (produto.getPreco() == null || produto.getPreco().doubleValue() <= 0) {
            throw new IllegalArgumentException("Preço do produto deve ser maior que zero");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0) {
            throw new IllegalArgumentException("Estoque do produto deve ser informado e não negativo");
        }
        if (categoria == null || categoria.getId() == null) {
            throw new IllegalArgumentException("Categoria válida é obrigatória");
        }
    }
}
