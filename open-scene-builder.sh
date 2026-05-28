#!/bin/bash

# Script para abrir arquivos FXML no Scene Builder
# Uso: ./open-scene-builder.sh [nome do arquivo ou número]

FXML_DIR="src/main/resources/fxml"

# Lista de FXMLs disponíveis
declare -a FXMLS=("login" "dashboard" "venda" "produtos" "estoque" "caixa" "relatorios")

# Função para mostrar menu
show_menu() {
    echo "═══════════════════════════════════════════════════════"
    echo "  📱 ABRIR FXML NO SCENE BUILDER"
    echo "═══════════════════════════════════════════════════════"
    echo ""
    echo "Selecione um arquivo FXML:"
    echo ""
    for i in "${!FXMLS[@]}"; do
        idx=$((i + 1))
        printf "  %d) %-15s → %s.fxml\n" "$idx" "${FXMLS[$i]^}" "${FXMLS[$i]}"
    done
    echo ""
    echo "  0) Sair"
    echo ""
    echo "═══════════════════════════════════════════════════════"
}

# Se nenhum argumento, mostrar menu interativo
if [ -z "$1" ]; then
    show_menu
    read -p "Escolha uma opção [0-7]: " choice
else
    choice=$1
fi

# Validar entrada
if [ "$choice" -eq 0 ] 2>/dev/null; then
    echo "Saindo..."
    exit 0
elif [ "$choice" -ge 1 ] && [ "$choice" -le 7 ] 2>/dev/null; then
    file_idx=$((choice - 1))
    filename="${FXMLS[$file_idx]}.fxml"
    filepath="$FXML_DIR/$filename"
    
    if [ -f "$filepath" ]; then
        echo ""
        echo "📂 Abrindo: $filepath"
        echo "⏳ Aguarde, iniciando Scene Builder..."
        echo ""
        
        # Tentar abrir com Scene Builder
        if command -v scenebuilder &> /dev/null; then
            scenebuilder "$filepath" &
            echo "✅ Scene Builder aberto!"
        else
            echo "❌ Scene Builder não encontrado."
            echo ""
            echo "📌 Instale Scene Builder:"
            echo "   • Download: https://gluonhq.com/products/scene-builder/"
            echo "   • Ou via package manager:"
            echo "   • Ubuntu/Debian: sudo apt install openjfx-21-jfx"
            echo "   • macOS: brew install scene-builder"
            echo "   • Fedora: sudo dnf install openjfx-21"
            exit 1
        fi
    else
        echo "❌ Arquivo não encontrado: $filepath"
        exit 1
    fi
else
    echo "❌ Opção inválida: $choice"
    show_menu
    exit 1
fi

echo ""
echo "💡 Dicas:"
echo "  • Use Ctrl+S ou Cmd+S para salvar alterações"
echo "  • Recompile com: ./mvnw clean compile"
echo "  • Execute com: ./mvnw javafx:run"
echo ""
echo "Para mais informações, veja: SCENE_BUILDER_GUIDE.md"
