package com.pdv.javafx.controller;

import com.pdv.model.Produto;
import com.pdv.service.ProdutoService;
import com.pdv.javafx.StageManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

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
    private TableColumn<Produto, ?> precoColumn;

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

        backButton.setOnAction(event ->
            stageManager.showScene("/fxml/dashboard.fxml", "PDV Dashboard", true)
        );

        searchButton.setOnAction(event -> buscarProdutos());

        novoProdutoButton.setOnAction(event -> abrirFormularioProduto());

        buscarProdutos();
    }

    private void buscarProdutos() {
        produtoTable.setItems(
            FXCollections.observableArrayList(produtoService.listarTodos())
        );
    }

    private void abrirFormularioProduto() {
        // Módulo de cadastro de produto será implementado em próxima iteração
    }
}