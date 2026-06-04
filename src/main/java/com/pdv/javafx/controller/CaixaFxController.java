package com.pdv.javafx.controller;

import com.pdv.model.Caixa;
import com.pdv.model.Funcionario;
import com.pdv.service.CaixaService;
import com.pdv.javafx.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Component;

@Component
public class CaixaFxController {

    @FXML
    private Button backButton;

    @FXML
    private Button abrirButton;

    @FXML
    private Button fecharButton;

    @FXML
    private TextField valorInicialField;

    @FXML
    private TextField valorFinalField;

    @FXML
    private TableView<Caixa> caixaTable;

    private final StageManager stageManager;
    private final CaixaService caixaService;

    public CaixaFxController(
            StageManager stageManager,
            CaixaService caixaService
    ) {
        this.stageManager = stageManager;
        this.caixaService = caixaService;
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(event ->
                stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true));

        abrirButton.setOnAction(event -> abrirCaixa());
        fecharButton.setOnAction(event -> fecharCaixa());

        carregarCaixas();
    }

    private void abrirCaixa() {
        try {
            // Autenticação temporária: solicita nome do operador
            Funcionario operador = solicitarAutenticacao();
            if (operador == null) {
                exibirErro("Autenticação cancelada");
                return;
            }

            Caixa caixa = new Caixa();
            caixa.setValorInicial(parseBigDecimal(valorInicialField.getText()));
            caixa.setFuncionario(operador);

            caixaService.abrirCaixa(caixa);
            exibirSucesso("Caixa aberto com sucesso!");

            carregarCaixas();
            valorInicialField.clear();

        } catch (Exception e) {
            exibirErro("Erro ao abrir caixa: " + e.getMessage());
        }
    }

    private void fecharCaixa() {
        try {
            Caixa caixaSelecionado = caixaTable.getSelectionModel().getSelectedItem();
            if (caixaSelecionado == null) {
                exibirErro("Selecione um caixa para fechar");
                return;
            }

            caixaService.fecharCaixa(
                    caixaSelecionado.getId(),
                    parseBigDecimal(valorFinalField.getText())
            );
            exibirSucesso("Caixa fechado com sucesso!");

            carregarCaixas();
            valorFinalField.clear();

        } catch (Exception e) {
            exibirErro("Erro ao fechar caixa: " + e.getMessage());
        }
    }

    private void carregarCaixas() {
        try {
            caixaTable.setItems(
                FXCollections.observableArrayList(caixaService.listarTodos())
            );
        } catch (Exception e) {
            exibirErro("Erro ao carregar caixas: " + e.getMessage());
        }
    }

    private Funcionario solicitarAutenticacao() {
        // Dialog simples para autenticação temporária
        Dialog<Funcionario> dialog = new Dialog<>();
        dialog.setTitle("Autenticação do Operador");
        dialog.setHeaderText("Digite seu nome para autenticar");
        dialog.setResizable(true);

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do operador");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        ButtonType autenticarButton = new ButtonType("Autenticar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelarButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().setAll(autenticarButton, cancelarButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == autenticarButton) {
                String nome = nomeField.getText();
                if (!nome.isBlank()) {
                    // Criar funcionário temporário
                    Funcionario funcionario = new Funcionario();
                    funcionario.setNome(nome);
                    funcionario.setId(System.currentTimeMillis()); // ID temporário
                    return funcionario;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private java.math.BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return java.math.BigDecimal.ZERO;
        }
        return new java.math.BigDecimal(value.replace(',', '.'));
    }
}