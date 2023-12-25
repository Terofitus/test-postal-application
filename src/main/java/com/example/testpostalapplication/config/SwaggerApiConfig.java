package com.example.testpostalapplication.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Postal application",
                description = "An application for registering mail items and managing their movement",
                version = "1.0.0",
                contact = @Contact(
                        name = "Alexander Frolov",
                        email = "frolovAlexandri@list.ru"
                )
        )
)
public class SwaggerApiConfig {
}