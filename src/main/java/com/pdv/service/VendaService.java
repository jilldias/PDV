package com.pdv.service;

import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.*;
import com.pdv.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;

    public VendaService(VendaRepository vendaRepository,
                        ProdutoService produtoService,
                        ClienteService clienteService,
                        FuncionarioService funcionarioService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
        this.clienteService = clienteService;
        this.funcionarioService = funcionarioService;
    }

    @Transactional
    public Venda registrarVenda(Venda venda) {
        if (venda == null || venda.getFuncionario() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Venda deve conter funcionário e itens");
        }

        Funcionario funcionario = funcionarioService.buscarPorId(venda.getFuncionario().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "id", venda.getFuncionario().getId()));

        Cliente cliente = null;
        if (venda.getCliente() != null && venda.getCliente().getId() != null) {
            cliente = clienteService.buscarPorId(venda.getCliente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", venda.getCliente().getId()));
        }

        List<ItemVenda> itens = venda.getItens().stream().map(item -> {
            Produto produto = produtoService.buscarPorId(item.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", item.getProduto().getId()));

            if (!Boolean.TRUE.equals(produto.getAtivo())) {
                throw new IllegalArgumentException("Produto inativo não pode ser vendido: " + produto.getNome());
            }
            if (produto.getEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para: " + produto.getNome());
            }

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            produtoService.diminuirEstoque(produto, item.getQuantidade());

            ItemVenda itemVenda = new ItemVenda();
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(item.getQuantidade());
            itemVenda.setPrecoUnitario(produto.getPreco());
            itemVenda.setSubtotal(subtotal);
            return itemVenda;
        }).collect(Collectors.toList());

        BigDecimal valorTotal = itens.stream()
                .map(ItemVenda::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venda.setFuncionario(funcionario);
        venda.setCliente(cliente);
        venda.setItens(itens);
        venda.setValorTotal(valorTotal);
        venda.setDataVenda(LocalDateTime.now());
        venda.setStatus(VendaStatus.PROCESSADO);

        Venda vendaSalva = vendaRepository.save(venda);
        vendaSalva.getItens().forEach(item -> item.setVenda(vendaSalva));
        return vendaSalva;
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda", "id", id));
    }

    public List<Venda> listarVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }
}
