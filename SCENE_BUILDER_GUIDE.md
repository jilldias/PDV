# Guia: Abrindo FXML no Scene Builder

## 📋 Arquivos FXML Disponíveis

O projeto possui os seguintes arquivos FXML prontos para edição no Scene Builder:

```
src/main/resources/fxml/
├── login.fxml          → Tela de login
├── dashboard.fxml      → Dashboard principal com navegação
├── venda.fxml          → Interface de vendas (PDV)
├── produtos.fxml       → Gestão de produtos
├── estoque.fxml        → Controle de estoque
├── caixa.fxml          → Gestão de caixa
└── relatorios.fxml     → Relatórios
```

## 🚀 Como Abrir no Scene Builder

### Opção 1: Usar VS Code com Extensão FXML

1. Instale a extensão **JavaFX Scene Builder** no VS Code
2. Clique com botão direito em qualquer arquivo `.fxml`
3. Selecione **"Open with Scene Builder"**

### Opção 2: Abrir Scene Builder Manualmente

1. Abra a aplicação **JavaFX Scene Builder**
2. Vá em `File` → `Open`
3. Navegue até: `/home/dev-jill/VsCode/Java_Vs/PDVJILL/src/main/resources/fxml/`
4. Selecione um arquivo `.fxml` e clique em `Abrir`

### Opção 3: Linha de Comando

```bash
# Instalar Scene Builder (se necessário)
scenebuilder /path/to/arquivo.fxml

# Ou via Maven (se configurado)
./mvnw javafx:jfxmod
```

## 📐 Estrutura dos FXMLs

Todos os FXMLs foram reescritos com:

✅ **BorderPane** como raiz para layouts responsivos  
✅ **fx:id** em todos os componentes para vinculação automática  
✅ **GridPane / HBox / VBox** para layouts estruturados  
✅ **Imports** completos para todas as classes JavaFX  
✅ **Stylesheet** referenciando `/css/style.css`  
✅ **Controller** vinculado automaticamente via `fx:controller`  

## 🎨 Editando no Scene Builder

### Para Adicionar Novos Componentes:

1. Abra o arquivo FXML no Scene Builder
2. Arraste componentes da **Library** (esquerda) para o **Canvas** (centro)
3. Configure propriedades no **Inspector** (direita)
4. Vinc ule componentes aos **@FXML** do Controller:
   - Clique no componente
   - Vá em **Code** → **fx:id**
   - Digite o mesmo nome do campo `@FXML private Button` no Controller

### Exemplo de Binding:

**Em dashboard.fxml:**
```xml
<Button fx:id="vendaButton" text="Nova Venda" />
```

**Em DashboardController.java:**
```java
@FXML
private Button vendaButton;

@FXML
public void initialize() {
    vendaButton.setOnAction(event -> stageManager.showScene("/fxml/venda.fxml", "PDV", true));
}
```

## 💡 Controllers Atualizados

Todos os controllers agora são Spring `@Component` com:

- ✅ Injeção de dependência via construtor
- ✅ Anotações `@FXML` em todos os campos
- ✅ Método `initialize()` para configurar eventos
- ✅ Integração com `StageManager` para navegação

**Controllers disponíveis:**
- `LoginController` → autenticação
- `DashboardController` → menu principal
- `VendaController` → ponto de venda
- `ProdutoFxController` → gestão de produtos
- `EstoqueController` → movimentações
- `CaixaController` → abertura/fechamento
- `RelatorioController` → relatórios

## 🔄 Fluxo de Navegação

```
login.fxml (LoginController)
    ↓
dashboard.fxml (DashboardController)
    ├→ venda.fxml (VendaController)
    ├→ produtos.fxml (ProdutoFxController)
    ├→ estoque.fxml (EstoqueController)
    ├→ caixa.fxml (CaixaController)
    └→ relatorios.fxml (RelatorioController)
```

## 🛠️ Compilação e Execução

Após editar um FXML:

```bash
# Compilar projeto
./mvnw clean compile

# Executar aplicação
./mvnw javafx:run
# ou
java -m javafx.launcher com.pdv.javafx.JavaFxApplication
```

## ⚙️ Configuração Recomendada no Scene Builder

1. **Preferences:**
   - Ativar **Snap to Grid** para alinhamento
   - Ativar **Show Grid** para visualizar grid
   - Definir **Grid Size** como 10px

2. **Hierarchy:**
   - Use a aba Hierarchy para selecionar componentes
   - Reordene elementos arrastando

3. **Inspector:**
   - Configure **Layout** para responsividade
   - Adicione **StyleClass** para aplicar CSS
   - Defina **fx:id** para vinculação automática

## 📝 Dicas

- **Sempre compilar após edições** para validar sintaxe FXML
- **Usar `VBox.vgrow="ALWAYS"`** para componentes que devem crescer
- **Usar `HBox.hgrow="ALWAYS"`** para componentes horizontais
- **Respeitar a estrutura BorderPane** para melhor responsividade
- **Testar navegação** entre as telas depois de salvar

## 🐛 Troubleshooting

**Erro: "fx:id not found"**
→ Certifique-se de adicionar a anotação `@FXML` no Controller

**Erro: "Controller class not found"**
→ Verifique se o package está correto em `fx:controller="com.pdv.javafx.controller.NomeController"`

**FXML não carrega**
→ Execute `./mvnw clean compile` antes de abrir no Scene Builder

---

**Agora você está pronto para editar as interfaces visuais com Scene Builder!** 🎉
