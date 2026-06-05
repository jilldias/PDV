package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.model.Caixa;
import com.pdv.model.Funcionario;
import com.pdv.service.FuncionarioService;
import com.pdv.service.CaixaService;
import com.pdv.javafx.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @FXML
    private TableColumn<Caixa, Long> idColumn;

    @FXML
    private TableColumn<Caixa, String> statusColumn;

    @FXML
    private TableColumn<Caixa, LocalDateTime> dataAberturaColumn;

    @FXML
    private TableColumn<Caixa, BigDecimal> valorInicialColumn;

    @FXML
    private TableColumn<Caixa, BigDecimal> valorFinalColumn;

    private final StageManager stageManager;
    private final CaixaService caixaService;
    private final FuncionarioService funcionarioService;
    private final SessionInfo sessionInfo;

    public CaixaFxController(
            StageManager stageManager,
            CaixaService caixaService,
            FuncionarioService funcionarioService,
            SessionInfo sessionInfo
    ) {
        this.stageManager = stageManager;
        this.caixaService = caixaService;
        this.funcionarioService = funcionarioService;
        this.sessionInfo = sessionInfo;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getStatus() != null
                                ? cellData.getValue().getStatus().name()
                                : ""));
        dataAberturaColumn.setCellValueFactory(new PropertyValueFactory<>("dataAbertura"));
        valorInicialColumn.setCellValueFactory(new PropertyValueFactory<>("valorInicial"));
        valorFinalColumn.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));

        backButton.setOnAction(event ->
                stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true));

        abrirButton.setOnAction(event -> abrirCaixa());
        fecharButton.setOnAction(event -> fecharCaixa());

        carregarCaixas();
    }

    private void abrirCaixa() {
        try {
            Funcionario operador = obterOperador();
            if (operador == null) {
                exibirErro("Nenhum usuário autenticado para abrir o caixa");
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

            BigDecimal valorFinal = valorFinalField.getText() == null || valorFinalField.getText().isBlank()
                    ? caixaSelecionado.getValorInicial().add(caixaService.calcularTotalVendas(caixaSelecionado))
                    : parseBigDecimal(valorFinalField.getText());

            caixaService.fecharCaixa(caixaSelecionado.getId(), valorFinal);
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

    private Funcionario obterOperador() {
        if (sessionInfo.hasAuthenticatedUser()) {
            return sessionInfo.getAuthenticatedUser();
        }

        return funcionarioService.buscarPorLogin("admin").orElse(null);
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
