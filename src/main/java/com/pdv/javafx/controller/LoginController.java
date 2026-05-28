package com.pdv.javafx.controller;

import com.pdv.auth.AuthService;
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

    private final AuthService authService;
    private final StageManager stageManager;

    public LoginController(AuthService authService, StageManager stageManager) {
        this.authService = authService;
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
            errorMessage.setText("Preencha login e senha");
            return;
        }

        try {
            authService.authenticate(login, senha);
            stageManager.showScene("/fxml/dashboard.fxml", "Dashboard PDV", true);
        } catch (Exception e) {
            errorMessage.setText("Login ou senha inválidos");
        }
    }
}
