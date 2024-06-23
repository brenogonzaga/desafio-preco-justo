package br.app.precojusto.controllers;

import br.app.precojusto.models.dto.response.RelatorioResponseDTO;
import br.app.precojusto.services.PatoService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
@Tag(name = "Relatórios")
public class RelatorioController {

    @Autowired
    private PatoService patoService;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/gerar")
    public ResponseEntity<byte[]> gerarRelatorio() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório de Patos");
        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Nome");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        headerRow.createCell(3).setCellValue("Status");
        headerRow.createCell(4).setCellValue("Cliente");
        headerRow.createCell(5).setCellValue("Tipo de Cliente");
        headerRow.createCell(6).setCellValue("Valor");

        List<RelatorioResponseDTO> patosOrganizados = patoService.obterPatosOrganizados();

        for (RelatorioResponseDTO pato : patosOrganizados) {
            rowNum = preencherLinha(sheet, pato, rowNum, 0);
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResponse.setContentDispositionFormData("filename", "relatorio_patos.xlsx");

        return new ResponseEntity<>(baos.toByteArray(), headersResponse, HttpStatus.OK);
    }

    private int preencherLinha(Sheet sheet, RelatorioResponseDTO pato, int rowNum, int nivel) {
        Row row = sheet.createRow(rowNum++);

        // Preenche o nome de acordo com o nível hierárquico
        if (nivel == 0) {
            row.createCell(0).setCellValue(pato.getNome());
        } else if (nivel == 1) {
            row.createCell(1).setCellValue(pato.getNome());
        } else if (nivel == 2) {
            row.createCell(2).setCellValue(pato.getNome());
        }

        // Preenche as outras colunas com os dados do pato
        row.createCell(3).setCellValue(pato.getStatus());
        row.createCell(4).setCellValue(pato.getCliente());
        row.createCell(5).setCellValue(pato.getTipoCliente());
        row.createCell(6).setCellValue(pato.getValor());

        // Chama recursivamente para preencher os filhos, aumentando o nível hierárquico
        for (RelatorioResponseDTO filho : pato.getFilhos()) {
            rowNum = preencherLinha(sheet, filho, rowNum, nivel + 1);
        }

        return rowNum;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> relatorioPdf() throws JRException, IOException {
        byte[] relatorioBytes = gerarRelatorioPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "relatorio_patos.pdf");

        return new ResponseEntity<>(relatorioBytes, headers, HttpStatus.OK);
    }

    private List<RelatorioResponseDTO> organizarPatosParaRelatorio(List<RelatorioResponseDTO> patos) {
        List<RelatorioResponseDTO> todosPatos = new ArrayList<>();
        for (RelatorioResponseDTO pato : patos) {
            adicionarPatoETodosFilhos(todosPatos, pato, 0);
        }
        return todosPatos;
    }

    private void adicionarPatoETodosFilhos(List<RelatorioResponseDTO> lista, RelatorioResponseDTO pato, int nivel) {
        pato.setNivel(nivel); // Adiciona o nível ao PatoDTO
        lista.add(pato);
        for (RelatorioResponseDTO filho : pato.getFilhos()) {
            adicionarPatoETodosFilhos(lista, filho, nivel + 1);
        }
    }

    public byte[] gerarRelatorioPDF() throws JRException, IOException {
        Resource resource = resourceLoader.getResource("classpath:PDF.jrxml");
        InputStream inputStream = resource.getInputStream();

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        List<RelatorioResponseDTO> dados = patoService.obterPatosOrganizados();
        List<RelatorioResponseDTO> dadosParaRelatorio = organizarPatosParaRelatorio(dados);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dadosParaRelatorio);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @GetMapping
    public ResponseEntity<List<RelatorioResponseDTO>> obterPatosOrganizados() {
        return ResponseEntity.ok(patoService.obterPatosOrganizados());
    }
}
