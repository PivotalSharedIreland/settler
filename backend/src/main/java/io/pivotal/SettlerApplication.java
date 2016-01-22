package io.pivotal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SettlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettlerApplication.class, args);
    }

    @Bean
    public PropertyProvider propertyProvider() {
        return new InMemoryPropertyProvider();
    }
}



