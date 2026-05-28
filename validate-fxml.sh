#!/bin/bash

# Script para validar todos os FXMLs
# Verifica sintaxe, bem-formação XML, e conformidade Scene Builder

set -e

FXML_DIR="src/main/resources/fxml"
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}  ✓ Validador de FXML - Scene Builder Compliance${NC}"
echo -e "${BLUE}════════════════════════════════════════════════════════${NC}"
echo ""

# Função para validar XML bem-formado
validate_xml() {
    local file=$1
    if python3 -c "import xml.etree.ElementTree as ET; ET.parse('$file')" 2>/dev/null; then
        echo -e "${GREEN}  ✓ XML bem-formado${NC}"
        return 0
    else
        echo -e "${RED}  ✗ Erro de sintaxe XML${NC}"
        return 1
    fi
}

# Função para verificar BorderPane
check_borderPane() {
    local file=$1
    if grep -q "BorderPane" "$file"; then
        echo -e "${GREEN}  ✓ BorderPane como root${NC}"
        return 0
    else
        echo -e "${RED}  ✗ Sem BorderPane como root${NC}"
        return 1
    fi
}

# Função para verificar fx:controller
check_controller() {
    local file=$1
    if grep -q 'fx:controller=' "$file"; then
        controller=$(grep -oP 'fx:controller="\K[^"]+' "$file" | head -1)
        echo -e "${GREEN}  ✓ Controller vinculado: $controller${NC}"
        return 0
    else
        echo -e "${RED}  ✗ Sem controller vinculado${NC}"
        return 1
    fi
}

# Função para verificar fx:id
check_fxid() {
    local file=$1
    count=$(grep -o 'fx:id=' "$file" | wc -l)
    if [ $count -gt 0 ]; then
        echo -e "${GREEN}  ✓ $count componentes com fx:id${NC}"
        return 0
    else
        echo -e "${YELLOW}  ⚠ Nenhum fx:id encontrado${NC}"
        return 1
    fi
}

# Função para verificar imports
check_imports() {
    local file=$1
    if grep -q '<?import' "$file"; then
        count=$(grep -c '<?import' "$file")
        echo -e "${GREEN}  ✓ $count imports de JavaFX${NC}"
        return 0
    else
        echo -e "${RED}  ✗ Sem imports de JavaFX${NC}"
        return 1
    fi
}

# Função para verificar linhas
check_lines() {
    local file=$1
    lines=$(wc -l < "$file")
    echo -e "${BLUE}  📊 $lines linhas${NC}"
}

# Validar cada FXML
passed=0
failed=0

for fxml_file in "$FXML_DIR"/*.fxml; do
    if [ -f "$fxml_file" ]; then
        filename=$(basename "$fxml_file")
        echo ""
        echo -e "${YELLOW}📄 Validando: $filename${NC}"
        
        errors=0
        
        # Validações
        validate_xml "$fxml_file" || ((errors++))
        check_borderPane "$fxml_file" || ((errors++))
        check_controller "$fxml_file" || ((errors++))
        check_fxid "$fxml_file" || ((errors++))
        check_imports "$fxml_file" || ((errors++))
        check_lines "$fxml_file"
        
        if [ $errors -eq 0 ]; then
            echo -e "${GREEN}  ✓ VALIDAÇÃO COMPLETA${NC}"
            ((passed++))
        else
            echo -e "${RED}  ✗ $errors ERRO(S) ENCONTRADO(S)${NC}"
            ((failed++))
        fi
    fi
done

echo ""
echo -e "${BLUE}════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}  📊 RESUMO${NC}"
echo -e "${BLUE}════════════════════════════════════════════════════════${NC}"
echo -e "  ${GREEN}Arquivos Válidos: $passed${NC}"
echo -e "  ${RED}Arquivos com Erro: $failed${NC}"
echo -e "  ${YELLOW}Total: $((passed + failed))${NC}"
echo ""

if [ $failed -eq 0 ]; then
    echo -e "${GREEN}✅ Todos os FXMLs estão prontos para Scene Builder!${NC}"
    exit 0
else
    echo -e "${RED}❌ Alguns FXMLs precisam ser corrigidos${NC}"
    exit 1
fi
