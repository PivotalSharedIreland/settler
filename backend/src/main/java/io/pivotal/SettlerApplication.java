package io.pivotal;

import io.pivotal.property.PersistentPropertyProvider;
import io.pivotal.property.PropertyProvider;
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
        return new PersistentPropertyProvider();
    }
}



