package com.pdv.javafx.controller;

import com.pdv.service.ProdutoService;
import com.pdv.service.RelatorioService;
import com.pdv.service.VendaService;
import com.pdv.javafx.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
public class RelatorioController {

    @FXML
    private Button backButton;

    @FXML
    private Button vendasReportButton;

    @FXML
    private Button estoqueReportButton;

    @FXML
    private Button exportarVendasPdfButton;

    @FXML
    private TextArea reportArea;

    private final StageManager stageManager;
    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final RelatorioService relatorioService;

    public RelatorioController(StageManager stageManager, VendaService vendaService, ProdutoService produtoService, RelatorioService relatorioService) {
        this.stageManager = stageManager;
        this.vendaService = vendaService;
        this.produtoService = produtoService;
        this.relatorioService = relatorioService;
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true));
        vendasReportButton.setOnAction(event -> gerarResumoVendas());
        estoqueReportButton.setOnAction(event -> gerarRelatorioEstoque());
        exportarVendasPdfButton.setOnAction(event -> exportarVendasPdf());
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
                .append(" - Pagamento: ").append(v.getFormaPagamento())
                .append(" - Troco: R$ ").append(v.getTroco() == null ? java.math.BigDecimal.ZERO : v.getTroco())
                .append("\n")
                .append(descreverItens(v))
                .append("\n"));
        reportArea.setText(texto.toString());
    }

    private String descreverItens(com.pdv.model.Venda venda) {
        var texto = new StringBuilder();
        venda.getItens().forEach(item -> texto.append("  ")
                .append(item.getProduto() != null ? item.getProduto().getNome() : "")
                .append(" | Qtd: ").append(item.getQuantidade())
                .append(" | Unit.: R$ ").append(item.getPrecoUnitario())
                .append(" | Subtotal: R$ ").append(item.getSubtotal())
                .append("\n"));
        return texto.toString();
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

    private void exportarVendasPdf() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Exportar vendas em PDF");
        chooser.setInitialFileName("relatorio-vendas.pdf");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File arquivo = chooser.showSaveDialog(reportArea.getScene().getWindow());
        if (arquivo == null) {
            return;
        }

        try {
            Files.write(arquivo.toPath(), relatorioService.exportarRelatorioVendasPdf(vendaService.listarTodas()));
            exibirInfo("Relatório exportado para: " + arquivo.getAbsolutePath());
        } catch (IOException e) {
            exibirErro("Erro ao salvar PDF: " + e.getMessage());
        }
    }

    private void exibirInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatórios");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
