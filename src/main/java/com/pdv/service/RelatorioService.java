package com.pdv.service;

import com.pdv.model.ItemVenda;
import com.pdv.model.Produto;
import com.pdv.model.Venda;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RelatorioService {

    public byte[] exportarRelatorioVendasPdf(List<Venda> vendas) {
        try (PDDocument documento = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage pagina = new PDPage();
            documento.addPage(pagina);

            try (PDPageContentStream conteudo = new PDPageContentStream(documento, pagina)) {
                conteudo.beginText();
                conteudo.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                conteudo.newLineAtOffset(50, 750);
                conteudo.showText("Relatório de Vendas");
                conteudo.endText();

                int y = 720;
                conteudo.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                for (Venda venda : vendas) {
                    if (y < 100) {
                        conteudo.close();
                        pagina = new PDPage();
                        documento.addPage(pagina);
                        y = 750;
                    }
                    conteudo.beginText();
                    conteudo.newLineAtOffset(50, y);
                    conteudo.showText(String.format("Venda %d | Funcionário %s | Total: R$ %s", venda.getId(), venda.getFuncionario().getNome(), venda.getValorTotal().toPlainString()));
                    conteudo.endText();
                    y -= 18;
                }
            }

            documento.save(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Falha ao gerar relatório em PDF", ex);
        }
    }

    public byte[] exportarEstoqueExcel(List<Produto> produtos) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("Estoque");
            int rowNum = 0;
            Row header = sheet.createRow(rowNum++);
            String[] colunas = {"ID", "Produto", "Categoria", "Preço", "Estoque", "Ativo"};
            for (int i = 0; i < colunas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(colunas[i]);
            }

            for (Produto produto : produtos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(produto.getId());
                row.createCell(1).setCellValue(produto.getNome());
                row.createCell(2).setCellValue(produto.getCategoria() != null ? produto.getCategoria().getNome() : "");
                row.createCell(3).setCellValue(produto.getPreco().doubleValue());
                row.createCell(4).setCellValue(produto.getEstoque());
                row.createCell(5).setCellValue(produto.getAtivo() ? "Sim" : "Não");
            }

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Falha ao gerar relatório em Excel", ex);
        }
    }
}
