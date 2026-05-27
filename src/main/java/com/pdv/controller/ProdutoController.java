package com.pdv.controller;

import com.pdv.dto.ProdutoRequestDTO;
import com.pdv.dto.ProdutoResponseDTO;
import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Categoria;
import com.pdv.model.Produto;
import com.pdv.service.CategoriaService;
import com.pdv.service.ProdutoService;
import com.pdv.util.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;

    public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodos().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
        return ResponseEntity.ok(DtoMapper.toDTO(produto));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO requestDTO) {
        Categoria categoria = categoriaService.buscarPorId(requestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", requestDTO.getCategoriaId()));
        Produto produto = DtoMapper.toEntity(requestDTO, categoria);
        Produto criado = produtoService.criarProduto(produto, categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO requestDTO) {
        Categoria categoria = categoriaService.buscarPorId(requestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", requestDTO.getCategoriaId()));
        Produto produto = DtoMapper.toEntity(requestDTO, categoria);
        Produto atualizado = produtoService.atualizarProduto(id, produto, categoria);
        return ResponseEntity.ok(DtoMapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }
}
