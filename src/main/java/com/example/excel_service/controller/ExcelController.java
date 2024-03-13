package com.example.excel_service.controller;

import com.example.excel_service.models.Code;
import com.example.excel_service.services.ExcelService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@CrossOrigin("*")
public class ExcelController {

    @Autowired
    ExcelService excelService;

//    @Operation(summary = "Trả ra file new code yêu cầu đầu vào 2 file xlsx có cột thứ 2 là code cả 2 file đều có tiêu đề")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Trả ra file có new code",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = Code.class)) }),
//            @ApiResponse(responseCode = "500", description = "Hệ thống có vấn đề",
//                    content = @Content)})
    @PostMapping(value = "/get_new_code",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> getFile(@RequestPart("file") List<MultipartFile> files
    ) throws IOException {
        String filename = "hang.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.load(files));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping(value = "/hang",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> getFileProduct(@RequestPart("file") List<MultipartFile> files
    ) throws IOException {
        String filename = "hang.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.getFileProduct(files));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

//    @Operation(summary = "Trả ra file có mã hàng hóa và tên hàng hóa")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Trả ra file có mã hàng hóa và tên hàng hóa",
//                    content = { @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = Code.class)) }),
//            @ApiResponse(responseCode = "500", description = "Hệ thống có vấn đề",
//                    content = @Content)})
    @PostMapping(value = "/get_code_name",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Resource> getHang(@RequestPart("file") MultipartFile files
    ) throws IOException {
        String filename = "hang.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.getFilePro(files));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> sayHelloToAdmin() {
        return ResponseEntity.ok("Hello Admin");
    }

    @GetMapping("/user")
    public ResponseEntity<String> sayHelloToUser() {
        return ResponseEntity.ok("Hello User");
    }
}
