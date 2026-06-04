package com.pdv.javafx.controller;

import com.pdv.javafx.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

@Component
public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button entrarButton;

    @FXML
    private Text errorMessage;

    private final StageManager stageManager;

    public LoginController(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    @FXML
    public void initialize() {
        entrarButton.setOnAction(event -> autenticar());
    }

    private void autenticar() {
        String login = loginField.getText();
        String senha = senhaField.getText();

        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            exibirErro("Preencha login e senha");
            loginField.requestFocus();
            return;
        }

        try {
            // Autenticação temporária: aceita qualquer login/senha não vazio
            // TODO: Implementar autenticação real com banco de dados
            if (!login.isBlank() && !senha.isBlank()) {
                senhaField.clear();
                loginField.clear();
                stageManager.showScene(
                        "/fxml/dashboard.fxml",
                        "Dashboard PDV",
                        true
                );
            } else {
                exibirErro("Login ou senha inválidos");
                senhaField.clear();
                loginField.requestFocus();
            }
        } catch (Exception e) {
            exibirErro("Erro ao autenticar. Tente novamente.");
            e.printStackTrace();
        }
    }

    private void exibirErro(String mensagem) {
        errorMessage.setText(mensagem);
        errorMessage.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
    }
}
