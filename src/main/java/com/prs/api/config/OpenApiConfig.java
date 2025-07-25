package com.prs.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
        		.info(apiInfo())
//        		.servers(Arrays.asList(
//                        new Server().url("http://localhost:8080").description("Local Dev Server"),
//                        new Server().url("https://api.example.com").description("Production Server")
//                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }
    
    private Info apiInfo() {
        return new Info()
                .title("PRS API Documentation")
                .version("1.0")
                .description("PRS USER API documentation for the application")
                .termsOfService("https://example.com/terms")
                .contact(new Contact()
                        .name("PRS API Support")
                        .url("https://example.com/contact")
                        .email("support@prs.com"));
    }
}