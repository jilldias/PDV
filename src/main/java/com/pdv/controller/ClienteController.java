package com.pdv.controller;

import com.pdv.dto.ClienteRequestDTO;
import com.pdv.dto.ClienteResponseDTO;
import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Cliente;
import com.pdv.service.ClienteService;
import com.pdv.util.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
        return ResponseEntity.ok(DtoMapper.toDTO(cliente));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        Cliente cliente = clienteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(DtoMapper.toDTO(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO requestDTO) {
        Cliente criado = clienteService.criarCliente(DtoMapper.toEntity(requestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO requestDTO) {
        Cliente atualizado = clienteService.atualizarCliente(id, DtoMapper.toEntity(requestDTO));
        return ResponseEntity.ok(DtoMapper.toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        clienteService.removerCliente(id);
        return ResponseEntity.noContent().build();
    }
}
