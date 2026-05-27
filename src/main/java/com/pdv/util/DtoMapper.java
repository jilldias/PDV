package com.pdv.util;

import com.pdv.dto.*;
import com.pdv.model.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static Produto toEntity(ProdutoRequestDTO requestDTO, Categoria categoria) {
        if (requestDTO == null) {
            return null;
        }
        Produto produto = new Produto();
        produto.setNome(requestDTO.getNome());
        produto.setCodigoBarras(requestDTO.getCodigoBarras());
        produto.setPreco(requestDTO.getPreco());
        produto.setEstoque(requestDTO.getEstoque());
        produto.setAtivo(requestDTO.getAtivo() == null ? Boolean.TRUE : requestDTO.getAtivo());
        produto.setCategoria(categoria);
        return produto;
    }

    public static ProdutoResponseDTO toDTO(Produto produto) {
        if (produto == null) {
            return null;
        }
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setCodigoBarras(produto.getCodigoBarras());
        dto.setPreco(produto.getPreco());
        dto.setEstoque(produto.getEstoque());
        dto.setAtivo(produto.getAtivo());
        dto.setDataCadastro(produto.getDataCadastro());
        dto.setCategoriaId(produto.getCategoria() != null ? produto.getCategoria().getId() : null);
        dto.setCategoriaNome(produto.getCategoria() != null ? produto.getCategoria().getNome() : null);
        return dto;
    }

    public static Categoria toEntity(CategoriaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Categoria categoria = new Categoria();
        categoria.setNome(requestDTO.getNome());
        categoria.setDescricao(requestDTO.getDescricao());
        return categoria;
    }

    public static CategoriaResponseDTO toDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setDescricao(categoria.getDescricao());
        return dto;
    }

    public static Cliente toEntity(ClienteRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNome(requestDTO.getNome());
        cliente.setCpf(requestDTO.getCpf());
        cliente.setTelefone(requestDTO.getTelefone());
        cliente.setEmail(requestDTO.getEmail());
        cliente.setEndereco(requestDTO.getEndereco());
        return cliente;
    }

    public static ClienteResponseDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setTelefone(cliente.getTelefone());
        dto.setEmail(cliente.getEmail());
        dto.setEndereco(cliente.getEndereco());
        return dto;
    }

    public static Funcionario toEntity(FuncionarioRequestDTO requestDTO, String senhaCriptografada) {
        if (requestDTO == null) {
            return null;
        }
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(requestDTO.getNome());
        funcionario.setCargo(requestDTO.getCargo());
        funcionario.setLogin(requestDTO.getLogin());
        funcionario.setSenha(senhaCriptografada);
        funcionario.setRole(requestDTO.getRole());
        funcionario.setEmail(requestDTO.getEmail());
        funcionario.setTelefone(requestDTO.getTelefone());
        return funcionario;
    }

    public static FuncionarioResponseDTO toDTO(Funcionario funcionario) {
        if (funcionario == null) {
            return null;
        }
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(funcionario.getId());
        dto.setNome(funcionario.getNome());
        dto.setCargo(funcionario.getCargo());
        dto.setLogin(funcionario.getLogin());
        dto.setRole(funcionario.getRole());
        dto.setEmail(funcionario.getEmail());
        dto.setTelefone(funcionario.getTelefone());
        return dto;
    }

    public static ItemVenda toEntity(ItemVendaRequestDTO requestDTO, Produto produto) {
        if (requestDTO == null || produto == null) {
            return null;
        }
        BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(requestDTO.getQuantidade()));
        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(requestDTO.getQuantidade());
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(subtotal);
        return item;
    }

    public static ItemVendaResponseDTO toDTO(ItemVenda itemVenda) {
        if (itemVenda == null) {
            return null;
        }
        ItemVendaResponseDTO dto = new ItemVendaResponseDTO();
        dto.setId(itemVenda.getId());
        dto.setProdutoId(itemVenda.getProduto() != null ? itemVenda.getProduto().getId() : null);
        dto.setProdutoNome(itemVenda.getProduto() != null ? itemVenda.getProduto().getNome() : null);
        dto.setQuantidade(itemVenda.getQuantidade());
        dto.setPrecoUnitario(itemVenda.getPrecoUnitario());
        dto.setSubtotal(itemVenda.getSubtotal());
        return dto;
    }

    public static VendaResponseDTO toDTO(Venda venda) {
        if (venda == null) {
            return null;
        }
        VendaResponseDTO dto = new VendaResponseDTO();
        dto.setId(venda.getId());
        dto.setDataVenda(venda.getDataVenda());
        dto.setTotal(venda.getValorTotal());
        dto.setFuncionarioId(venda.getFuncionario() != null ? venda.getFuncionario().getId() : null);
        dto.setFuncionarioNome(venda.getFuncionario() != null ? venda.getFuncionario().getNome() : null);
        dto.setClienteId(venda.getCliente() != null ? venda.getCliente().getId() : null);
        dto.setClienteNome(venda.getCliente() != null ? venda.getCliente().getNome() : null);
        dto.setFormaPagamento(venda.getFormaPagamento());
        dto.setStatus(venda.getStatus());
        dto.setItens(venda.getItens().stream().map(DtoMapper::toDTO).collect(Collectors.toList()));
        return dto;
    }

    public static CaixaResponseDTO toDTO(Caixa caixa) {
        if (caixa == null) {
            return null;
        }
        CaixaResponseDTO dto = new CaixaResponseDTO();
        dto.setId(caixa.getId());
        dto.setDataAbertura(caixa.getDataAbertura());
        dto.setDataFechamento(caixa.getDataFechamento());
        dto.setValorInicial(caixa.getValorInicial());
        dto.setValorFinal(caixa.getValorFinal());
        dto.setStatus(caixa.getStatus());
        dto.setFuncionarioId(caixa.getFuncionario() != null ? caixa.getFuncionario().getId() : null);
        dto.setFuncionarioNome(caixa.getFuncionario() != null ? caixa.getFuncionario().getNome() : null);
        return dto;
    }
}
