package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.javafx.StageManager;
import com.pdv.model.Venda;
import com.pdv.service.CaixaService;
import com.pdv.service.ProdutoService;
import com.pdv.service.VendaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Component
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
    private TableView<Venda> vendasRecentesTable;

    @FXML
    private TableColumn<Venda, Long> vendaIdColumn;

    @FXML
    private TableColumn<Venda, String> vendaFuncionarioColumn;

    @FXML
    private TableColumn<Venda, String> vendaPagamentoColumn;

    @FXML
    private TableColumn<Venda, BigDecimal> vendaTotalColumn;

    @FXML
    private Button vendaButton;

    @FXML
    private Button produtosButton;

    @FXML
    private Button caixaButton;

    @FXML
    private Button relatoriosButton;

    private final StageManager stageManager;
    private final SessionInfo sessionInfo;
    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private final CaixaService caixaService;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public DashboardController(StageManager stageManager,
                               SessionInfo sessionInfo,
                               ProdutoService produtoService,
                               VendaService vendaService,
                               CaixaService caixaService) {
        this.stageManager = stageManager;
        this.sessionInfo = sessionInfo;
        this.produtoService = produtoService;
        this.vendaService = vendaService;
        this.caixaService = caixaService;
    }

    @FXML
    public void initialize() {
        vendaIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vendaFuncionarioColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(
                        () -> cellData.getValue().getFuncionario() != null
                                ? cellData.getValue().getFuncionario().getNome()
                                : ""));
        vendaPagamentoColumn.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        vendaTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        vendaTotalColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(BigDecimal valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? "" : currencyFormat.format(valor));
            }
        });
        vendasRecentesTable.setRowFactory(table -> {
            TableRow<Venda> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    exibirVenda(row.getItem());
                }
            });
            return row;
        });

        vendaButton.setOnAction(event -> stageManager.showScene("/fxml/venda.fxml", "PDV - Ponto de Venda", true));
        produtosButton.setOnAction(event -> stageManager.showScene("/fxml/produtos.fxml", "Produtos PDV", true));
        caixaButton.setOnAction(event -> stageManager.showScene("/fxml/caixa.fxml", "Caixa PDV", true));
        relatoriosButton.setOnAction(event -> stageManager.showScene("/fxml/relatorios.fxml", "Relatórios PDV", true));

        atualizarMetricas();
    }

    private void atualizarMetricas() {
        var vendas = vendaService.listarTodas();
        var produtos = produtoService.listarTodos();

        totalVendidoLabel.setText(currencyFormat.format(vendas.stream()
                .map(v -> v.getValorTotal())
                .filter(v -> v != null)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)));
        produtosCadastradosLabel.setText(String.valueOf(produtos.size()));
        vendasDiaLabel.setText(String.valueOf(vendas.stream()
                .filter(v -> v.getDataVenda() != null && v.getDataVenda().toLocalDate().equals(LocalDate.now()))
                .count()));
        estoqueBaixoLabel.setText(String.valueOf(produtos.stream().filter(p -> p.getEstoque() != null && p.getEstoque() < 5).count()));

        vendasRecentesTable.setItems(FXCollections.observableArrayList(vendas));
    }

    private void exibirVenda(Venda venda) {
        StringBuilder descricao = new StringBuilder();
        descricao.append("Venda #").append(venda.getId()).append("\n");
        descricao.append("Funcionario: ").append(venda.getFuncionario() != null ? venda.getFuncionario().getNome() : "").append("\n");
        descricao.append("Forma de pagamento: ").append(venda.getFormaPagamento()).append("\n");
        descricao.append("Total: ").append(currencyFormat.format(venda.getValorTotal())).append("\n");
        descricao.append("Troco: ").append(currencyFormat.format(venda.getTroco() == null ? BigDecimal.ZERO : venda.getTroco())).append("\n\n");
        descricao.append("Produtos:\n");
        venda.getItens().forEach(item -> descricao.append("- ")
                .append(item.getProduto() != null ? item.getProduto().getNome() : "")
                .append(" | Qtd: ").append(item.getQuantidade())
                .append(" | Unit.: ").append(currencyFormat.format(item.getPrecoUnitario()))
                .append(" | Subtotal: ").append(currencyFormat.format(item.getSubtotal()))
                .append("\n"));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venda");
        alert.setHeaderText("Descricao da venda");
        alert.setContentText(descricao.toString());
        alert.showAndWait();
    }
}
