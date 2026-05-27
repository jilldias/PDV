package com.pdv.controller;

import com.pdv.dto.CaixaRequestDTO;
import com.pdv.dto.CaixaResponseDTO;
import com.pdv.model.Caixa;
import com.pdv.model.Funcionario;
import com.pdv.service.CaixaService;
import com.pdv.util.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/caixas")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @GetMapping
    public ResponseEntity<List<CaixaResponseDTO>> listarTodos() {
        List<CaixaResponseDTO> caixas = caixaService.listarTodos().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(caixas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaixaResponseDTO> buscarPorId(@PathVariable Long id) {
        Caixa caixa = caixaService.buscarPorId(id);
        return ResponseEntity.ok(DtoMapper.toDTO(caixa));
    }

    @PostMapping("/abrir")
    public ResponseEntity<CaixaResponseDTO> abrirCaixa(@Valid @RequestBody CaixaRequestDTO requestDTO) {
        Caixa caixa = new Caixa();
        caixa.setValorInicial(requestDTO.getValorInicial());
        caixa.setStatus(requestDTO.getStatus() != null ? com.pdv.model.CaixaStatus.valueOf(requestDTO.getStatus()) : null);
        caixa.setFuncionario(new Funcionario(requestDTO.getFuncionarioId()));
        Caixa aberto = caixaService.abrirCaixa(caixa);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDTO(aberto));
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<CaixaResponseDTO> fecharCaixa(@PathVariable Long id, @RequestParam BigDecimal valorFinal) {
        Caixa fechado = caixaService.fecharCaixa(id, valorFinal);
        return ResponseEntity.ok(DtoMapper.toDTO(fechado));
    }
}
