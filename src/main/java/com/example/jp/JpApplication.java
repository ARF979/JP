package com.example.jp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JpApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpApplication.class, args);
    }

}
