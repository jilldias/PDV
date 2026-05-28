package com.pdv.javafx.controller;

import com.pdv.model.MovimentacaoEstoque;
import com.pdv.service.EstoqueService;
import com.pdv.javafx.StageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class EstoqueController {

    @FXML
    private Button backButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<MovimentacaoEstoque> movimentacaoTable;

    private final StageManager stageManager;
    private final EstoqueService estoqueService;

    public EstoqueController(StageManager stageManager, EstoqueService estoqueService) {
        this.stageManager = stageManager;
        this.estoqueService = estoqueService;
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true));
        searchButton.setOnAction(event -> atualizarMovimentacoes());
        atualizarMovimentacoes();
    }

    private void atualizarMovimentacoes() {
        movimentacaoTable.setItems(FXCollections.observableArrayList(estoqueService.listarMovimentacoes()));
    }
}
