package com.example.excel_service.services;

import com.example.excel_service.models.Code;
import com.example.excel_service.models.Product;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Service
public class ExcelService {

    public ByteArrayInputStream load(List<MultipartFile> files) {
        List<String> file1Codes = getListCode(files.get(0), "Sheet1");
        List<String> file2Codes = getListCode(files.get(1), "Sheet1");
        List<String> newCodes = file2Codes.stream().filter(item -> !file1Codes.contains(item)).collect(Collectors.toList());
        return ExcelHelper.codesToExcel(newCodes);
    }

    public ByteArrayInputStream getFileProduct(List<MultipartFile> files) {
        List<Code> file1Codes = getListCodeModel(files.get(0), "Sheet1");

        Map<String, Product> file2Codes = getListProduct(files.get(1), "Sheet1");

        List<Product> result = file1Codes.stream().map(item -> {
            Product product = file2Codes.get(item.getStt());
            product.setCode(item.getCode());
            return product;
        }).collect(Collectors.toList());
        return ExcelHelper.codeModelsToExcel(result);
    }

    public ByteArrayInputStream getFilePro(MultipartFile files) {
        List<Product> file1Codes = getListPro(files, "TKX");
        return ExcelHelper.codeModelsToExcel(file1Codes);
    }

    List<String> getListCode(MultipartFile file, String sheetName) {
        List<String> result = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());


            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 1:
                            String code = currentCell.getStringCellValue();
                            if (code != null && !code.trim().isEmpty()) {
                                result.add(currentCell.getStringCellValue());
                            }
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    Map<String, Product> getListProduct(MultipartFile file, String sheetName) {
        Map<String, Product> result = new HashMap<>();
        DataFormatter formatter = new DataFormatter();

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());


            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Product product = new Product();
                Iterator<Cell> cellsInRow = currentRow.iterator();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            String stt = formatter.formatCellValue(currentCell);
                            if (stt != null && !stt.trim().isEmpty()) {
                                product.setStt(stt);
                            }
                            break;
                        case 1:
                            String name = formatter.formatCellValue(currentCell);
                            if (name != null && !name.trim().isEmpty()) {
                                product.setName(name);
                            }
                            break;
                        case 3:
                            String maHS = formatter.formatCellValue(currentCell);
                            if (maHS != null && !maHS.trim().isEmpty()) {
                                product.setMaHS(maHS);
                            }
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (product.getStt() != null && !product.getStt().isEmpty()) {
                    result.put(product.getStt(), product);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }


    List<Code> getListCodeModel(MultipartFile file, String sheetName) {
        DataFormatter formatter = new DataFormatter();

        List<Code> result = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());


            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Code product = new Code();
                Iterator<Cell> cellsInRow = currentRow.iterator();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            String name = formatter.formatCellValue(currentCell);
                            if (name != null && !name.trim().isEmpty()) {
                                product.setCode(name);
                            }
                            break;
                        case 1:
                            String stt = formatter.formatCellValue(currentCell);
                            if (stt != null && !stt.trim().isEmpty()) {
                                product.setStt(stt);
                            }
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }
                if (product.getCode() != null && !product.getCode().isEmpty()) {
                    result.add(product);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    List<Product> getListPro(MultipartFile file, String sheetName) {
        List<Product> result = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(file.getInputStream());


            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;
            String maHS = null, name = null;
            int index = 1;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Product product = new Product();
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Boolean isReadyMaHS = false, isReadyName = false;
                while (cellsInRow.hasNext()) {
                    Cell cell = cellsInRow.next();

                    switch (cell.getCellType()) {

                        case STRING:
                            String cellValue = cell.getRichStringCellValue().getString();
                            if ("Mã số hàng hóa".equals(cellValue.trim())) {
                                isReadyMaHS = true;
                                break;
                            }
                            if ("Mô tả hàng hóa".equals(cellValue.trim())) {
                                isReadyName = true;
                                break;
                            }
                            if (isReadyMaHS && !cellValue.isEmpty()) {
                                maHS = cellValue.trim();
                                isReadyMaHS = false;
                                break;
                            }
                            if (isReadyName && !cellValue.isEmpty()) {
                                name = cellValue.trim();
                                isReadyName = false;
                                break;
                            }
                            break;

                        default:
                            break;
                    }
                }
                if (maHS != null && name != null && !name.isEmpty()) {
                    result.add(Product.builder().name(name).maHS(maHS).stt(String.valueOf(index)).build());
                    name = null;
                    maHS = null;
                    index++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
