package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.javafx.StageManager;
import com.pdv.model.Venda;
import com.pdv.service.CaixaService;
import com.pdv.service.ProdutoService;
import com.pdv.service.VendaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

@Component
public class DashboardController {

    @FXML
    private Label totalVendidoLabel;

    @FXML
    private Label produtosCadastradosLabel;

    @FXML
    private Label vendasDiaLabel;

    @FXML
    private Label estoqueBaixoLabel;

    @FXML
    private TableView<Venda> vendasRecentesTable;

    @FXML
    private Button vendaButton;

    @FXML
    private Button produtosButton;

    @FXML
    private Button estoqueButton;

    @FXML
    private Button caixaButton;

    @FXML
    private Button relatoriosButton;

    private final StageManager stageManager;
    private final SessionInfo sessionInfo;
    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private final CaixaService caixaService;

    public DashboardController(StageManager stageManager,
                               SessionInfo sessionInfo,
                               ProdutoService produtoService,
                               VendaService vendaService,
                               CaixaService caixaService) {
        this.stageManager = stageManager;
        this.sessionInfo = sessionInfo;
        this.produtoService = produtoService;
        this.vendaService = vendaService;
        this.caixaService = caixaService;
    }

    @FXML
    public void initialize() {
        vendaButton.setOnAction(event -> stageManager.showScene("/fxml/venda.fxml", "PDV - Ponto de Venda", true));
        produtosButton.setOnAction(event -> stageManager.showScene("/fxml/produtos.fxml", "Produtos PDV", true));
        estoqueButton.setOnAction(event -> stageManager.showScene("/fxml/estoque.fxml", "Estoque PDV", true));
        caixaButton.setOnAction(event -> stageManager.showScene("/fxml/caixa.fxml", "Caixa PDV", true));
        relatoriosButton.setOnAction(event -> stageManager.showScene("/fxml/relatorios.fxml", "Relatórios PDV", true));

        atualizarMetricas();
    }

    private void atualizarMetricas() {
        var vendas = vendaService.listarTodas();
        var produtos = produtoService.listarTodos();

        totalVendidoLabel.setText("R$ " + vendas.stream()
                .map(v -> v.getValorTotal())
                .filter(v -> v != null)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        produtosCadastradosLabel.setText(String.valueOf(produtos.size()));
        vendasDiaLabel.setText(String.valueOf(vendas.size()));
        estoqueBaixoLabel.setText(String.valueOf(produtos.stream().filter(p -> p.getEstoque() != null && p.getEstoque() < 5).count()));

        vendasRecentesTable.setItems(FXCollections.observableArrayList(vendas));
    }
}
