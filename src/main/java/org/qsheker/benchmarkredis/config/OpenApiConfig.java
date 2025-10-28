package org.qsheker.benchmarkredis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI usersOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .description("Documentation for testing app")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("qSheker")
                                .email("aldikzhaks@gmail.com")));
    }
}
