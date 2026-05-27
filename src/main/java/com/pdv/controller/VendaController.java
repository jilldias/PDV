package com.pdv.controller;

import com.pdv.dto.ItemVendaRequestDTO;
import com.pdv.dto.VendaRequestDTO;
import com.pdv.dto.VendaResponseDTO;
import com.pdv.exception.ResourceNotFoundException;
import com.pdv.model.Cliente;
import com.pdv.model.Funcionario;
import com.pdv.model.ItemVenda;
import com.pdv.model.Produto;
import com.pdv.model.Venda;
import com.pdv.service.ProdutoService;
import com.pdv.service.VendaService;
import com.pdv.util.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class VendaController {

    private final VendaService vendaService;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarTodas() {
        List<VendaResponseDTO> vendas = vendaService.listarTodas().stream()
                .map(DtoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable Long id) {
        Venda venda = vendaService.buscarPorId(id);
        return ResponseEntity.ok(DtoMapper.toDTO(venda));
    }

    @PostMapping
    public ResponseEntity<VendaResponseDTO> criar(@Valid @RequestBody VendaRequestDTO requestDTO) {
        List<ItemVenda> itens = requestDTO.getItens().stream().map(this::toItemVenda).collect(Collectors.toList());
        Venda venda = new Venda();
        venda.setFuncionario(requestDTO.getFuncionarioId() != null ? new Funcionario(requestDTO.getFuncionarioId()) : null);
        venda.setCliente(requestDTO.getClienteId() != null ? new Cliente(requestDTO.getClienteId()) : null);
        venda.setFormaPagamento(requestDTO.getFormaPagamento());
        venda.setItens(itens);

        Venda salvo = vendaService.registrarVenda(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoMapper.toDTO(salvo));
    }

    private ItemVenda toItemVenda(ItemVendaRequestDTO requestDTO) {
        Produto produto = produtoService.buscarPorId(requestDTO.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", requestDTO.getProdutoId()));
        ItemVenda itemVenda = new ItemVenda();
        itemVenda.setProduto(produto);
        itemVenda.setQuantidade(requestDTO.getQuantidade());
        return itemVenda;
    }
}
