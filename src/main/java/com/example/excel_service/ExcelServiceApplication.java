package com.example.excel_service;

//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@OpenAPIDefinition(servers = {@Server(url = "https://9589-42-116-243-247.ap.ngrok.io", description = "Default Server URL")})
@SpringBootApplication
public class ExcelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelServiceApplication.class, args);
    }

}
