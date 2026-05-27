package com.pdv.util;

import com.pdv.dto.ProdutoRequestDTO;
import com.pdv.dto.ProdutoResponseDTO;
import com.pdv.model.Categoria;
import com.pdv.model.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {
        // Utilitário de mapeamento, não instanciável
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
        produto.setCategoria(categoria);
        produto.setAtivo(requestDTO.getAtivo() == null ? Boolean.TRUE : requestDTO.getAtivo());
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

    public static void updateEntity(Produto produto, ProdutoRequestDTO requestDTO, Categoria categoria) {
        if (produto == null || requestDTO == null) {
            return;
        }
        produto.setNome(requestDTO.getNome());
        produto.setCodigoBarras(requestDTO.getCodigoBarras());
        produto.setPreco(requestDTO.getPreco());
        produto.setEstoque(requestDTO.getEstoque());
        produto.setCategoria(categoria);
        produto.setAtivo(requestDTO.getAtivo() == null ? Boolean.TRUE : requestDTO.getAtivo());
    }
}
