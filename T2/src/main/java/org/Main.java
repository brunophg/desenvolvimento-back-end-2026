package org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.api", "org.dao", "org.model"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}