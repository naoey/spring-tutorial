package main.java.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "main.java")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}