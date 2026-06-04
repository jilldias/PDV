package com.pdv.javafx.controller;

import com.pdv.javafx.StageManager;
import com.pdv.model.Categoria;
import com.pdv.model.Produto;
import com.pdv.service.CategoriaService;
import com.pdv.service.ProdutoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CadastroProdutoController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField codigoBarrasField;

    @FXML
    private TextField precoField;

    @FXML
    private TextField estoqueField;

    @FXML
    private ComboBox<Categoria> categoriaCombo;

    @FXML
    private CheckBox ativoCheck;

    @FXML
    private Button voltarButton;

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final StageManager stageManager;

    public CadastroProdutoController(
            ProdutoService produtoService,
            CategoriaService categoriaService,
            StageManager stageManager) {

        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.stageManager = stageManager;
    }

    @FXML
    public void initialize() {
        // Configurar botão voltar UMA ÚNICA VEZ
        voltarButton.setOnAction(event -> stageManager.showScene(
                "/fxml/produtos.fxml",
                "Produtos",
                true));

        // Carregar categorias
        carregarCategorias();

        // Definir estado inicial
        ativoCheck.setSelected(true);
    }

    private void carregarCategorias() {
        try {
            categoriaCombo.setItems(
                    FXCollections.observableArrayList(
                            categoriaService.listarTodas()));

            categoriaCombo.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Categoria item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNome());
                }
            });

            categoriaCombo.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Categoria item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNome());
                }
            });
        } catch (Exception e) {
            exibirErro("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    @FXML
    private void salvarProduto() {
        try {
            // Validação
            if (nomeField.getText().isBlank()) {
                exibirErro("Nome do produto é obrigatório");
                return;
            }
            if (codigoBarrasField.getText().isBlank()) {
                exibirErro("Código de barras é obrigatório");
                return;
            }
            if (categoriaCombo.getValue() == null) {
                exibirErro("Selecione uma categoria");
                return;
            }

            Produto produto = new Produto();
            produto.setNome(nomeField.getText());
            produto.setCodigoBarras(codigoBarrasField.getText());
            produto.setPreco(new BigDecimal(precoField.getText()));
            produto.setEstoque(Integer.parseInt(estoqueField.getText()));
            produto.setAtivo(ativoCheck.isSelected());

            Categoria categoria = categoriaCombo.getValue();

            produtoService.criarProduto(produto, categoria);

            exibirSucesso("Produto salvo com sucesso!");
            limparCampos();

        } catch (NumberFormatException ex) {
            exibirErro("Preço e estoque devem ser números válidos");
        } catch (Exception ex) {
            exibirErro("Erro ao salvar produto: " + ex.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        nomeField.clear();
        codigoBarrasField.clear();
        precoField.clear();
        estoqueField.clear();
        categoriaCombo.setValue(null);
        ativoCheck.setSelected(true);
    }

    private void exibirSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}