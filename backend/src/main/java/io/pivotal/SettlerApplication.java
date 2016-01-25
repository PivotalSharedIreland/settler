package io.pivotal;

import com.google.common.collect.Lists;
import io.pivotal.property.PersistentPropertyProvider;
import io.pivotal.property.PropertyProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class    SettlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettlerApplication.class, args);
    }

    @Bean
    public PropertyProvider propertyProvider() {
        return new PersistentPropertyProvider();
    }


    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}



