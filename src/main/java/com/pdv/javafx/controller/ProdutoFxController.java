package com.pdv.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProdutoFxController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button novoProdutoButton;

    @FXML
    private TableView<?> produtoTable;

    @FXML
    private TableColumn<?, ?> nomeColumn;

    @FXML
    private TableColumn<?, ?> categoriaColumn;

    @FXML
    private TableColumn<?, ?> precoColumn;

    @FXML
    private TableColumn<?, ?> estoqueColumn;

    public void initialize() {
        searchButton.setOnAction(event -> buscarProdutos());
        novoProdutoButton.setOnAction(event -> abrirFormularioProduto());
    }

    private void buscarProdutos() {
        // Integração com API REST futura
    }

    private void abrirFormularioProduto() {
        // Abrir tela de cadastro/edição de produto
    }
}
