package com.pdv.javafx.controller;

import com.pdv.model.Produto;
import com.pdv.service.ProdutoService;
import com.pdv.javafx.StageManager;
import com.pdv.javafx.state.ProdutoFormState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class ProdutoFxController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private Button novoProdutoButton;

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
    private TableColumn<Produto, String> codigoBarrasColumn;

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
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public ProdutoFxController(ProdutoService produtoService, StageManager stageManager, ProdutoFormState produtoFormState) {
        this.produtoService = produtoService;
        this.stageManager = stageManager;
        this.produtoFormState = produtoFormState;
    }

    @FXML
    public void initialize() {

        // Configurar colunas da tabela
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        categoriaColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getCategoria() != null ? 
                      cellData.getValue().getCategoria().getNome() : "N/A"
            )
        );
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        estoqueColumn.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        precoColumn.setCellFactory(formatarMoeda());
        estoqueColumn.setCellFactory(formatarInteiro());

        backButton.setOnAction(event ->
            stageManager.showScene("/fxml/dashboard.fxml", "PDV Dashboard", true)
        );

        searchButton.setOnAction(event -> buscarProdutos());

        novoProdutoButton.setOnAction(event -> abrirFormularioProduto());
        alterarProdutoButton.setOnAction(event -> abrirAlteracaoProduto());
        produtoTable.getSelectionModel().selectedItemProperty().addListener((obs, anterior, atual) -> {
            produtoSelecionado = atual;
            produtoSelecionadoLabel.setText(atual == null ? "Nenhum produto selecionado" : "Selecionado: " + atual.getNome());
        });
        produtoTable.setRowFactory(table -> {
            TableRow<Produto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    produtoSelecionado = row.getItem();
                    abrirAlteracaoProduto();
                }
            });
            return row;
        });

        buscarProdutos();
    }

    private void buscarProdutos() {
        try {
            String termo = searchField.getText();
            var produtos = termo == null || termo.isBlank()
                    ? produtoService.listarTodos()
                    : produtoService.buscarPorNomeOuCodigo(termo.trim());
            produtoTable.setItems(
                FXCollections.observableArrayList(produtos)
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

    private Callback<TableColumn<Produto, BigDecimal>, TableCell<Produto, BigDecimal>> formatarMoeda() {
        return column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? "" : currencyFormat.format(valor));
            }
        };
    }

    private Callback<TableColumn<Produto, Integer>, TableCell<Produto, Integer>> formatarInteiro() {
        return column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? "" : valor.toString());
            }
        };
    }
}
