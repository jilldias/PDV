package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.javafx.StageManager;
import com.pdv.javafx.util.CarrinhoCompras;
import com.pdv.model.ItemVenda;
import com.pdv.model.Produto;
import com.pdv.service.ProdutoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
    private TableView<ItemVenda> vendasTable;

    @FXML
    private TableColumn<ItemVenda, String> produtoColumn;

    @FXML
    private TableColumn<ItemVenda, Integer> qtdColumn;

    @FXML
    private TableColumn<ItemVenda, BigDecimal> precoColumn;

    @FXML
    private TableColumn<ItemVenda, BigDecimal> subtotalColumn;

    private final StageManager stageManager;
    private final SessionInfo sessionInfo;
    private final ProdutoService produtoService;
    private final CarrinhoCompras carrinho;
    private Produto produtoSelecionado;

    public VendaController(StageManager stageManager, SessionInfo sessionInfo, ProdutoService produtoService) {
        this.stageManager = stageManager;
        this.sessionInfo = sessionInfo;
        this.produtoService = produtoService;
        this.carrinho = new CarrinhoCompras();
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

        // Configurar colunas da tabela
        produtoColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getProduto().getNome()
            )
        );
        qtdColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createObjectBinding(
                () -> cellData.getValue().getQuantidade()
            )
        );
        precoColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createObjectBinding(
                () -> cellData.getValue().getPrecoUnitario()
            )
        );
        subtotalColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createObjectBinding(
                () -> cellData.getValue().getSubtotal()
            )
        );

        vendasTable.setItems(carrinho.getItens());
    }

    private void buscarProduto() {
        String codigoBarras = codigoBarrasField.getText();
        if (codigoBarras == null || codigoBarras.isBlank()) {
            exibirErro("Informe o código de barras");
            codigoBarrasField.requestFocus();
            return;
        }

        var produto = produtoService.buscarPorCodigoBarras(codigoBarras);
        if (produto.isPresent()) {
            Produto p = produto.get();
            if (!p.getAtivo()) {
                exibirErro("Produto inativo não pode ser vendido");
                return;
            }
            this.produtoSelecionado = p;
            produtoInfoLabel.setText(String.format("%s - R$ %.2f (Estoque: %d)",
                    p.getNome(), p.getPreco(), p.getEstoque()));
            produtoInfoLabel.setStyle("-fx-text-fill: #27ae60;");
            codigoBarrasField.clear();
        } else {
            exibirErro("Produto não encontrado");
            codigoBarrasField.clear();
            codigoBarrasField.requestFocus();
        }
    }

    private void adicionarAoCarrinho() {
        if (produtoSelecionado == null) {
            exibirErro("Busque um produto primeiro");
            return;
        }

        Integer quantidade = quantidadeSpinner.getValue();
        if (quantidade == null || quantidade <= 0) {
            exibirErro("Quantidade inválida");
            return;
        }

        if (produtoSelecionado.getEstoque() < quantidade) {
            exibirErro(String.format("Estoque insuficiente. Disponível: %d", produtoSelecionado.getEstoque()));
            return;
        }

        try {
            carrinho.adicionarProduto(produtoSelecionado, quantidade);
            atualizarTotal();
            produtoSelecionado = null;
            produtoInfoLabel.setText("");
            quantidadeSpinner.getValueFactory().setValue(1);
            exibirSucesso(String.format("%s adicionado ao carrinho", produtoSelecionado.getNome()));
        } catch (Exception e) {
            exibirErro("Erro ao adicionar ao carrinho: " + e.getMessage());
        }
    }

    private void cancelarVenda() {
        if (carrinho.estaVazio()) {
            exibirErro("Carrinho já está vazio");
            return;
        }
        carrinho.limpar();
        atualizarTotal();
        produtoSelecionado = null;
        produtoInfoLabel.setText("");
        exibirSucesso("Venda cancelada");
    }

    private void finalizarVenda() {
        if (carrinho.estaVazio()) {
            exibirErro("Adicione produtos antes de finalizar");
            return;
        }

        String formaPagamento = formaPagamentoCombo.getValue();
        if (formaPagamento == null || formaPagamento.isBlank()) {
            exibirErro("Selecione a forma de pagamento");
            return;
        }

        try {
            // TODO: Integrar com VendaService para salvar a venda no BD
            BigDecimal total = carrinho.obterTotal();
            exibirSucesso(String.format("Venda finalizada! Total: R$ %.2f", total));
            carrinho.limpar();
            atualizarTotal();
            formaPagamentoCombo.getSelectionModel().clearSelection();
        } catch (Exception e) {
            exibirErro("Erro ao finalizar venda: " + e.getMessage());
        }
    }

    private void atualizarTotal() {
        BigDecimal total = carrinho.obterTotal();
        totalLabel.setText(String.format("R$ %.2f", total));
    }

    private void exibirErro(String mensagem) {
        produtoInfoLabel.setText(mensagem);
        produtoInfoLabel.setStyle("-fx-text-fill: #d9534f;");
    }

    private void exibirSucesso(String mensagem) {
        produtoInfoLabel.setText(mensagem);
        produtoInfoLabel.setStyle("-fx-text-fill: #27ae60;");
    }
}
