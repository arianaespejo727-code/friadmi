package com.friiadmi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.friiadmi")
public class TorneoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TorneoApplication.class, args);
    }
}   