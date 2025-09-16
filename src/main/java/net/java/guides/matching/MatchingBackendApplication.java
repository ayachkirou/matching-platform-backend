package net.java.guides.matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "net.java.guides.matching") // ← Important!
public class MatchingBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchingBackendApplication.class, args);
    }
}