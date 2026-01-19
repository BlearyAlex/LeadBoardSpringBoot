package com.alejandro.leadboardbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LeadBoardBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeadBoardBackendApplication.class, args);
    }

}
