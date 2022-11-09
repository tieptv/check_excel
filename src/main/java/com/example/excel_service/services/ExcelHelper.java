package com.example.excel_service.services;

import com.example.excel_service.models.Code;
import com.example.excel_service.models.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Code", "STT" };
    static String[] HEADER2s = { "Stt","Code", "Tên hàng", "Mã HS" };
    static String SHEET = "New_code";

    public static ByteArrayInputStream codesToExcel(List<Code> codes) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (Code code : codes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(code.getCode());
                row.createCell(1).setCellValue(code.getStt());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream codeModelsToExcel(List<Product> codes) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADER2s.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADER2s[col]);
            }

            int rowIdx = 1;
            for (Product p : codes) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getStt());
                row.createCell(1).setCellValue(p.getCode());
                row.createCell(2).setCellValue(p.getName());
                row.createCell(3).setCellValue(p.getMaHS());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
