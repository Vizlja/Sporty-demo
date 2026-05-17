package com.sporty.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sportyDemoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sporty Demo API")
                        .description(
                                "Sports betting event outcome handling and bet settlement (Kafka + RocketMQ).")
                        .version("1.0.0"));
    }
}
