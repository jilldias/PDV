package com.pdv.javafx.controller;

import com.pdv.model.MovimentacaoEstoque;
import com.pdv.service.EstoqueService;
import com.pdv.javafx.StageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    @FXML
    private TableColumn<MovimentacaoEstoque, String> produtoColumn;

    @FXML
    private TableColumn<MovimentacaoEstoque, Integer> quantidadeColumn;

    @FXML
    private TableColumn<MovimentacaoEstoque, String> tipoColumn;

    @FXML
    private TableColumn<MovimentacaoEstoque, LocalDateTime> dataColumn;

    private final StageManager stageManager;
    private final EstoqueService estoqueService;

    public EstoqueController(StageManager stageManager, EstoqueService estoqueService) {
        this.stageManager = stageManager;
        this.estoqueService = estoqueService;
    }

    @FXML
    public void initialize() {
        // Configurar colunas da tabela
        produtoColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getProduto() != null ? 
                      cellData.getValue().getProduto().getNome() : "N/A"
            )
        );
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        backButton.setOnAction(event -> 
            stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true)
        );
        
        searchButton.setOnAction(event -> atualizarMovimentacoes());
        
        atualizarMovimentacoes();
    }

    private void atualizarMovimentacoes() {
        try {
            movimentacaoTable.setItems(
                FXCollections.observableArrayList(estoqueService.listarMovimentacoes())
            );
        } catch (Exception e) {
            exibirErro("Erro ao carregar movimentações: " + e.getMessage());
        }
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
