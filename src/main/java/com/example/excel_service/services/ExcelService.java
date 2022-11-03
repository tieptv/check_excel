package com.example.excel_service.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    public ByteArrayInputStream load(List<MultipartFile> files) {
        List<String> file1Codes = getListCode(files.get(0), "Sheet1");
        List<String> file2Codes = getListCode(files.get(1), "Sheet1");
        List<String> newCodes = file2Codes.stream().filter(item -> !file1Codes.contains(item)).collect(Collectors.toList());
        return ExcelHelper.codesToExcel(newCodes);
    }

    List<String> getListCode(MultipartFile file, String sheetName) {
        List<String> result = new ArrayList<>();

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
        }
        finally {
            try {
                workbook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

}
