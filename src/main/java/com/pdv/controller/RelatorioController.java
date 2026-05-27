package com.pdv.controller;

import com.pdv.model.Produto;
import com.pdv.model.Venda;
import com.pdv.service.ProdutoService;
import com.pdv.service.RelatorioService;
import com.pdv.service.VendaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    public RelatorioController(RelatorioService relatorioService, VendaService vendaService, ProdutoService produtoService) {
        this.relatorioService = relatorioService;
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    @GetMapping("/vendas/pdf")
    public ResponseEntity<byte[]> exportarVendasPdf() {
        List<Venda> vendas = vendaService.listarTodas();
        byte[] content = relatorioService.exportarRelatorioVendasPdf(vendas);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-vendas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }

    @GetMapping("/estoque/excel")
    public ResponseEntity<byte[]> exportarEstoqueExcel() {
        List<Produto> produtos = produtoService.listarTodos();
        byte[] content = relatorioService.exportarEstoqueExcel(produtos);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-estoque.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(content);
    }
}
