package com.example.time.box;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableJpaRepositories
@EnableScheduling
@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true", methods = {})
public class TimeBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeBoxApplication.class, args);
    }

}
