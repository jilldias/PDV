package com.pdv.javafx.controller;

import com.pdv.model.Produto;
import com.pdv.service.ProdutoService;
import com.pdv.javafx.StageManager;
import com.pdv.javafx.state.ProdutoFormState;
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
    private Button selecionarProdutoButton;

    @FXML
    private Button alterarProdutoButton;

    @FXML
    private Button backButton;

    @FXML
    private Label produtoSelecionadoLabel;

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
    private final ProdutoFormState produtoFormState;
    private Produto produtoSelecionado;

    public ProdutoFxController(ProdutoService produtoService, StageManager stageManager, ProdutoFormState produtoFormState) {
        this.produtoService = produtoService;
        this.stageManager = stageManager;
        this.produtoFormState = produtoFormState;
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
        selecionarProdutoButton.setOnAction(event -> selecionarProduto());
        alterarProdutoButton.setOnAction(event -> abrirAlteracaoProduto());

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
        produtoFormState.novo();
        stageManager.showScene(
            "/fxml/cadastro_produto.fxml",
            "Cadastro Produto",
            true
        );
    }

    private void selecionarProduto() {
        produtoSelecionado = produtoTable.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            exibirErro("Selecione um produto na tabela");
            return;
        }

        produtoSelecionadoLabel.setText("Selecionado: " + produtoSelecionado.getNome());
    }

    private void abrirAlteracaoProduto() {
        if (produtoSelecionado == null) {
            selecionarProduto();
        }
        if (produtoSelecionado == null) {
            return;
        }

        produtoFormState.editar(produtoSelecionado);
        stageManager.showScene(
                "/fxml/cadastro_produto.fxml",
                "Alterar Produto",
                true
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
