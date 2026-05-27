package com.pdv.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class DashboardController {

    @FXML
    private Label totalVendidoLabel;

    @FXML
    private Label produtosCadastradosLabel;

    @FXML
    private Label vendasDiaLabel;

    @FXML
    private Label estoqueBaixoLabel;

    @FXML
    private TableView<?> vendasRecentesTable;

    public void initialize() {
        totalVendidoLabel.setText("R$ 0,00");
        produtosCadastradosLabel.setText("0");
        vendasDiaLabel.setText("0");
        estoqueBaixoLabel.setText("0");
    }
}
