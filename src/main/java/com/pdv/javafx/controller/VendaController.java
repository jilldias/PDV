package com.pdv.javafx.controller;

import com.pdv.auth.SessionInfo;
import com.pdv.javafx.StageManager;
import com.pdv.javafx.util.CarrinhoCompras;
import com.pdv.model.Funcionario;
import com.pdv.model.ItemVenda;
import com.pdv.model.Produto;
import com.pdv.model.Venda;
import com.pdv.service.FuncionarioService;
import com.pdv.service.ProdutoService;
import com.pdv.service.VendaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private TextField valorPagoField;

    @FXML
    private Label trocoLabel;

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

    @FXML
    private TableColumn<ItemVenda, Void> acaoColumn;

    private final StageManager stageManager;
    private final SessionInfo sessionInfo;
    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private final FuncionarioService funcionarioService;
    private final CarrinhoCompras carrinho;
    private Produto produtoSelecionado;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    public VendaController(StageManager stageManager,
                           SessionInfo sessionInfo,
                           ProdutoService produtoService,
                           VendaService vendaService,
                           FuncionarioService funcionarioService) {
        this.stageManager = stageManager;
        this.sessionInfo = sessionInfo;
        this.produtoService = produtoService;
        this.vendaService = vendaService;
        this.funcionarioService = funcionarioService;
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
                "CARTAO CREDITO",
                "CARTAO DEBITO",
                "PIX",
                "CHEQUE"
        ));
        formaPagamentoCombo.valueProperty().addListener((obs, anterior, atual) -> atualizarPagamentoDinheiro());
        valorPagoField.textProperty().addListener((obs, anterior, atual) -> atualizarTroco());

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
        precoColumn.setCellFactory(column -> formatarMoedaCell());
        subtotalColumn.setCellFactory(column -> formatarMoedaCell());
        acaoColumn.setCellFactory(column -> new TableCell<>() {
            private final Button removerButton = new Button("Remover");

            {
                removerButton.setOnAction(event -> {
                    ItemVenda item = getTableView().getItems().get(getIndex());
                    carrinho.getItens().remove(item);
                    atualizarTotal();
                    atualizarTroco();
                    vendasTable.refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removerButton);
            }
        });

        vendasTable.setItems(carrinho.getItens());
        atualizarPagamentoDinheiro();
    }

    private void buscarProduto() {
        String termo = codigoBarrasField.getText();
        if (termo == null || termo.isBlank()) {
            exibirErro("Informe o nome ou código de barras");
            codigoBarrasField.requestFocus();
            return;
        }

        var produto = produtoService.buscarPorCodigoBarras(termo.trim());
        if (produto.isPresent()) {
            selecionarProdutoEncontrado(produto.get());
            codigoBarrasField.clear();
            return;
        }

        List<Produto> encontrados = produtoService.buscarPorNomeOuCodigo(termo.trim());
        if (encontrados.isEmpty()) {
            exibirErro("Produto não encontrado");
            codigoBarrasField.requestFocus();
            return;
        }

        Produto escolhido = encontrados.size() == 1 ? encontrados.get(0) : escolherProduto(encontrados);
        if (escolhido != null) {
            selecionarProdutoEncontrado(escolhido);
            codigoBarrasField.clear();
        }
    }

    private Produto escolherProduto(List<Produto> produtos) {
        Map<String, Produto> opcoes = new LinkedHashMap<>();
        for (Produto produto : produtos) {
            opcoes.put(String.format("%s | %s | %s | Estoque: %d",
                    produto.getCodigoBarras(),
                    produto.getNome(),
                    currencyFormat.format(produto.getPreco()),
                    produto.getEstoque()), produto);
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(opcoes.keySet().iterator().next(), opcoes.keySet());
        dialog.setTitle("Selecionar produto");
        dialog.setHeaderText("Mais de um produto encontrado");
        dialog.setContentText("Produto:");
        return dialog.showAndWait().map(opcoes::get).orElse(null);
    }

    private void selecionarProdutoEncontrado(Produto produto) {
        if (!Boolean.TRUE.equals(produto.getAtivo())) {
            exibirErro("Produto inativo não pode ser vendido");
            return;
        }
        this.produtoSelecionado = produto;
        produtoInfoLabel.setText(String.format("%s - %s (Estoque: %d)",
                produto.getNome(), currencyFormat.format(produto.getPreco()), produto.getEstoque()));
        produtoInfoLabel.setStyle("-fx-text-fill: #27ae60;");
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
            String nomeProduto = produtoSelecionado.getNome();
            carrinho.adicionarProduto(produtoSelecionado, quantidade);
            vendasTable.refresh();
            atualizarTotal();
            atualizarTroco();
            produtoSelecionado = null;
            produtoInfoLabel.setText("");
            quantidadeSpinner.getValueFactory().setValue(1);
            exibirSucesso(String.format("%s adicionado ao carrinho", nomeProduto));
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
        valorPagoField.clear();
        atualizarTroco();
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
            Funcionario funcionario = obterFuncionario();
            if (funcionario == null) {
                exibirErro("Nenhum usuário autenticado para registrar a venda");
                return;
            }

            Venda venda = new Venda();
            venda.setFuncionario(funcionario);
            venda.setFormaPagamento(formaPagamento);
            if ("DINHEIRO".equalsIgnoreCase(formaPagamento)) {
                BigDecimal valorPago = parseBigDecimal(valorPagoField.getText());
                if (valorPago.compareTo(carrinho.obterTotal()) < 0) {
                    exibirErro("Valor pago menor que o total da venda");
                    return;
                }
                venda.setValorPago(valorPago);
            }
            venda.setItens(new ArrayList<>(carrinho.getItens()));

            Venda vendaSalva = vendaService.registrarVenda(venda);
            exibirSucesso(String.format("Venda %d finalizada! Total: %s | Troco: %s",
                    vendaSalva.getId(),
                    currencyFormat.format(vendaSalva.getValorTotal()),
                    currencyFormat.format(vendaSalva.getTroco() == null ? BigDecimal.ZERO : vendaSalva.getTroco())));
            carrinho.limpar();
            vendasTable.refresh();
            atualizarTotal();
            valorPagoField.clear();
            atualizarTroco();
            formaPagamentoCombo.getSelectionModel().clearSelection();
        } catch (Exception e) {
            exibirErro("Erro ao finalizar venda: " + e.getMessage());
        }
    }

    private Funcionario obterFuncionario() {
        if (sessionInfo.hasAuthenticatedUser()) {
            return sessionInfo.getAuthenticatedUser();
        }

        return funcionarioService.buscarPorLogin("admin").orElse(null);
    }

    private void atualizarTotal() {
        BigDecimal total = carrinho.obterTotal();
        totalLabel.setText(currencyFormat.format(total));
        atualizarTroco();
    }

    private void atualizarPagamentoDinheiro() {
        boolean dinheiro = "DINHEIRO".equalsIgnoreCase(formaPagamentoCombo.getValue());
        valorPagoField.setDisable(!dinheiro);
        if (!dinheiro) {
            valorPagoField.clear();
        }
        atualizarTroco();
    }

    private void atualizarTroco() {
        if (!"DINHEIRO".equalsIgnoreCase(formaPagamentoCombo.getValue())) {
            trocoLabel.setText(currencyFormat.format(BigDecimal.ZERO));
            return;
        }

        BigDecimal valorPago = parseBigDecimal(valorPagoField.getText());
        BigDecimal troco = valorPago.subtract(carrinho.obterTotal());
        trocoLabel.setText(currencyFormat.format(troco.max(BigDecimal.ZERO)));
    }

    private void exibirErro(String mensagem) {
        produtoInfoLabel.setText(mensagem);
        produtoInfoLabel.setStyle("-fx-text-fill: #d9534f;");
    }

    private void exibirSucesso(String mensagem) {
        produtoInfoLabel.setText(mensagem);
        produtoInfoLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    private TableCell<ItemVenda, BigDecimal> formatarMoedaCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? "" : currencyFormat.format(valor));
            }
        };
    }

    private BigDecimal parseBigDecimal(String valor) {
        if (valor == null || valor.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(valor.trim().replace(',', '.'));
    }
}
