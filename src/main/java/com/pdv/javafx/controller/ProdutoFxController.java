package com.pdv.javafx.controller;

import com.pdv.model.Produto;
import com.pdv.service.ProdutoService;
import com.pdv.javafx.StageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdutoFxController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button novoProdutoButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Produto> produtoTable;

    @FXML
    private TableColumn<Produto, String> nomeColumn;

    @FXML
    private TableColumn<Produto, String> categoriaColumn;

    @FXML
    private TableColumn<Produto, BigDecimal> precoColumn;

    @FXML
    private TableColumn<Produto, Integer> estoqueColumn;

    private final ProdutoService produtoService;
    private final StageManager stageManager;

    public ProdutoFxController(ProdutoService produtoService, StageManager stageManager) {
        this.produtoService = produtoService;
        this.stageManager = stageManager;
    }

    @FXML
    public void initialize() {

        // Configurar colunas da tabela
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        categoriaColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getCategoria() != null ? 
                      cellData.getValue().getCategoria().getNome() : "N/A"
            )
        );
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        estoqueColumn.setCellValueFactory(new PropertyValueFactory<>("estoque"));

        backButton.setOnAction(event ->
            stageManager.showScene("/fxml/dashboard.fxml", "PDV Dashboard", true)
        );

        searchButton.setOnAction(event -> buscarProdutos());

        novoProdutoButton.setOnAction(event -> abrirFormularioProduto());

        buscarProdutos();
    }

    private void buscarProdutos() {
        try {
            produtoTable.setItems(
                FXCollections.observableArrayList(produtoService.listarTodos())
            );
        } catch (Exception e) {
            exibirErro("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void abrirFormularioProduto() {
        // Usar resizable=true para manter na mesma Stage
        stageManager.showScene(
            "/fxml/cadastro_produto.fxml",
            "Cadastro Produto",
            true  // ← CORRIGIDO: Abre na mesma Stage
        );
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}