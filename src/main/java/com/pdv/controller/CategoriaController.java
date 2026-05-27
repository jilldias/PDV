package com.pdv.controller;

import com.pdv.dto.CategoriaRequestDTO;
import com.pdv.dto.CategoriaResponseDTO;
import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Categoria;
import com.pdv.service.CategoriaService;
import com.pdv.util.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", id));
        return ResponseEntity.ok(DtoMapper.toDTO(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO requestDTO) {
        Categoria criado = categoriaService.criarCategoria(DtoMapper.toEntity(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO requestDTO) {
        Categoria atualizado = categoriaService.atualizarCategoria(id, DtoMapper.toEntity(requestDTO));
        return ResponseEntity.ok(DtoMapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        categoriaService.removerCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
