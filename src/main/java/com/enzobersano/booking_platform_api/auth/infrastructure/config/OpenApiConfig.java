package com.enzobersano.booking_platform_api.auth.infrastructure.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Booking Platform API")
                        .description("""
                    DDD-based booking system with authentication and availability engine.
                    All protected endpoints require a Bearer JWT token obtained from /auth/login.
                    """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Enzo Bersano")
                                .email("enzo@bookingplatform.com")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}