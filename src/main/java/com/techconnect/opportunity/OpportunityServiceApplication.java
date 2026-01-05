package com.techconnect.opportunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpportunityServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpportunityServiceApplication.class, args);
    }
}
