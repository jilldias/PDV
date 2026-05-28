package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.javafx.StageManager;
import com.pdv.model.Produto;
import com.pdv.service.ProdutoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

@Component
public class VendaController {

    @FXML
    private Button backButton;

    @FXML
    private Button buscarButton;

    @FXML
    private Button adicionarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button finalizarButton;

    @FXML
    private TextField codigoBarrasField;

    @FXML
    private Spinner<Integer> quantidadeSpinner;

    @FXML
    private Label usuarioLabel;

    @FXML
    private Label produtoInfoLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private ComboBox<String> formaPagamentoCombo;

    @FXML
    private TableView<Produto> vendasTable;

    @FXML
    private TableColumn<Produto, String> produtoColumn;

    @FXML
    private TableColumn<Produto, Integer> qtdColumn;

    @FXML
    private TableColumn<Produto, ?> precoColumn;

    @FXML
    private TableColumn<Produto, ?> subtotalColumn;

    @FXML
    private TableColumn<Produto, ?> acaoColumn;

    private final StageManager stageManager;
    private final SessionInfo sessionInfo;
    private final ProdutoService produtoService;

    public VendaController(StageManager stageManager, SessionInfo sessionInfo, ProdutoService produtoService) {
        this.stageManager = stageManager;
        this.sessionInfo = sessionInfo;
        this.produtoService = produtoService;
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(event -> stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true));
        buscarButton.setOnAction(event -> buscarProduto());
        adicionarButton.setOnAction(event -> adicionarAoCarrinho());
        cancelarButton.setOnAction(event -> cancelarVenda());
        finalizarButton.setOnAction(event -> finalizarVenda());

        quantidadeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));

        formaPagamentoCombo.setItems(FXCollections.observableArrayList(
                "DINHEIRO",
                "CARTÃO CRÉDITO",
                "CARTÃO DÉBITO",
                "PIX",
                "CHEQUE"
        ));

        if (sessionInfo.hasAuthenticatedUser()) {
            usuarioLabel.setText(sessionInfo.getAuthenticatedUser().getNome());
        }

        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        qtdColumn.setCellValueFactory(new PropertyValueFactory<>("estoque"));
    }

    private void buscarProduto() {
        String codigoBarras = codigoBarrasField.getText();
        if (codigoBarras == null || codigoBarras.isBlank()) {
            produtoInfoLabel.setText("Informe o código de barras");
            return;
        }

        var produto = produtoService.buscarPorCodigoBarras(codigoBarras);
        if (produto.isPresent()) {
            Produto p = produto.get();
            produtoInfoLabel.setText(String.format("%s - R$ %.2f (Estoque: %d)",
                    p.getNome(), p.getPreco(), p.getEstoque()));
        } else {
            produtoInfoLabel.setText("Produto não encontrado");
        }
    }

    private void adicionarAoCarrinho() {
        produtoInfoLabel.setText("Função de adicionar ao carrinho em desenvolvimento");
    }

    private void cancelarVenda() {
        vendasTable.getItems().clear();
        codigoBarrasField.clear();
        totalLabel.setText("R$ 0,00");
    }

    private void finalizarVenda() {
        if (vendasTable.getItems().isEmpty()) {
            produtoInfoLabel.setText("Adicione produtos antes de finalizar");
            return;
        }
        produtoInfoLabel.setText("Venda finalizada com sucesso!");
        vendasTable.getItems().clear();
        totalLabel.setText("R$ 0,00");
    }
}
