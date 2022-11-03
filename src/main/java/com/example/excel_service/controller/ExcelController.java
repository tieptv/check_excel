package com.example.excel_service.controller;

import com.example.excel_service.services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @PostMapping("/check")
    public ResponseEntity<Resource> getFile(@RequestParam("file") List<MultipartFile> files
    ) throws IOException {
        String filename = "hang.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.load(files));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping("/hang")
    public ResponseEntity<Resource> getFileProduct(@RequestParam("file") List<MultipartFile> files
    ) throws IOException {
        String filename = "hang.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.getFileProduct(files));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping("/layhang")
    public ResponseEntity<Resource> getHang(@RequestParam("file") MultipartFile files
    ) throws IOException {
        String filename = "hang.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.getFilePro(files));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping("/check")
    public String hello(
    ) throws IOException {
        return "hello";
    }
}
