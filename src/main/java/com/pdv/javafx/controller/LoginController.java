package com.pdv.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button entrarButton;

    @FXML
    private Text errorMessage;

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

        errorMessage.setText("Autenticação local não implementada");
    }
}
