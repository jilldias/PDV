package com.pdv.javafx.controller;

import com.pdv.service.ProdutoService;
import com.pdv.service.VendaService;
import com.pdv.javafx.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.springframework.stereotype.Component;

@Component
public class RelatorioController {

    @FXML
    private Button backButton;

    @FXML
    private Button vendasReportButton;

    @FXML
    private Button estoqueReportButton;

    @FXML
    private TextArea reportArea;

    private final StageManager stageManager;
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    public RelatorioController(StageManager stageManager, VendaService vendaService, ProdutoService produtoService) {
        this.stageManager = stageManager;
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true));
        vendasReportButton.setOnAction(event -> gerarResumoVendas());
        estoqueReportButton.setOnAction(event -> gerarRelatorioEstoque());
    }

    private void gerarResumoVendas() {
        var vendas = vendaService.listarTodas();
        var total = vendas.stream()
                .map(v -> v.getValorTotal())
                .filter(v -> v != null)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        var texto = new StringBuilder();
        texto.append("Relatório de vendas\n");
        texto.append("Total de vendas: ").append(vendas.size()).append("\n");
        texto.append("Faturamento total: R$ ").append(total).append("\n\n");
        vendas.forEach(v -> texto.append("Venda #").append(v.getId())
                .append(" - Total: R$ ").append(v.getValorTotal())
                .append("\n"));
        reportArea.setText(texto.toString());
    }

    private void gerarRelatorioEstoque() {
        var produtos = produtoService.listarTodos();
        var texto = new StringBuilder();
        texto.append("Relatório de estoque\n");
        texto.append("Produtos cadastrados: ").append(produtos.size()).append("\n\n");
        produtos.forEach(p -> texto.append(p.getNome())
                .append(" - Estoque: ").append(p.getEstoque())
                .append("\n"));
        reportArea.setText(texto.toString());
    }
}
