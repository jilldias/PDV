package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.javafx.StageManager;
import com.pdv.model.Caixa;
import com.pdv.model.Funcionario;
import com.pdv.service.CaixaService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private final SessionInfo sessionInfo;

    public CaixaFxController(
            StageManager stageManager,
            CaixaService caixaService,
            SessionInfo sessionInfo
    ) {
        this.stageManager = stageManager;
        this.caixaService = caixaService;
        this.sessionInfo = sessionInfo;
    }

    @FXML
    public void initialize() {

        backButton.setOnAction(event ->
                stageManager.showScene(
                        "/fxml/dashboard.fxml",
                        "Dashboard PDV",
                        true
                )
        );

        abrirButton.setOnAction(event -> abrirCaixa());
        fecharButton.setOnAction(event -> fecharCaixa());

        carregarCaixas();
    }

    private void abrirCaixa() {

        try {

            if (!sessionInfo.hasAuthenticatedUser()) {
                throw new IllegalStateException("Usuário não autenticado");
            }

            Funcionario funcionario =
                    sessionInfo.getAuthenticatedUser();

            Caixa caixa = new Caixa();

            caixa.setValorInicial(
                    parseBigDecimal(valorInicialField.getText())
            );

            caixa.setFuncionario(funcionario);

            caixaService.abrirCaixa(caixa);

            carregarCaixas();

        } catch (Exception e) {

            System.err.println(
                    "Erro ao abrir caixa: " + e.getMessage()
            );
        }
    }

    private void fecharCaixa() {

        try {

            Caixa selecionado =
                    caixaTable.getSelectionModel().getSelectedItem();

            if (selecionado == null) {
                return;
            }

            caixaService.fecharCaixa(
                    selecionado.getId(),
                    parseBigDecimal(valorFinalField.getText())
            );

            carregarCaixas();

        } catch (Exception e) {

            System.err.println(
                    "Erro ao fechar caixa: " + e.getMessage()
            );
        }
    }

    private void carregarCaixas() {

        caixaTable.getItems().setAll(
                caixaService.listarTodos()
        );
    }

    private java.math.BigDecimal parseBigDecimal(String value) {

        if (value == null || value.isBlank()) {
            return java.math.BigDecimal.ZERO;
        }

        return new java.math.BigDecimal(
                value.replace(',', '.')
        );
    }
}
