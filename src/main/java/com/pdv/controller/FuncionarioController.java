package com.pdv.controller;

import com.pdv.dto.FuncionarioRequestDTO;
import com.pdv.dto.FuncionarioResponseDTO;
import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Funcionario;
import com.pdv.util.DtoMapper;
import com.pdv.service.FuncionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodos() {
        List<FuncionarioResponseDTO> funcionarios = funcionarioService.listarTodos().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario", "id", id));
        return ResponseEntity.ok(DtoMapper.toDTO(funcionario));
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> criar(@Valid @RequestBody FuncionarioRequestDTO requestDTO) {
        Funcionario funcionario = DtoMapper.toEntity(requestDTO, requestDTO.getSenha());
        Funcionario criado = funcionarioService.criarFuncionario(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody FuncionarioRequestDTO requestDTO) {
        Funcionario funcionario = DtoMapper.toEntity(requestDTO, requestDTO.getSenha());
        Funcionario atualizado = funcionarioService.atualizarFuncionario(id, funcionario);
        return ResponseEntity.ok(DtoMapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        funcionarioService.removerFuncionario(id);
        return ResponseEntity.noContent().build();
    }
}
