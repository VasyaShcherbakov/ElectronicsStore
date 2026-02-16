package com.OnlineElectronicsStore.OnlineElectronicsStore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Online Electronics Store API",
                version = "1.0",
                description = "Документація REST API магазину електроніки",
                contact = @Contact(
                        name = "Online Electronics Store Team",
                        email = "support@onlinestore.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://springdoc.org"
                )
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Введіть JWT токен у форматі: Bearer {token}"
)
public class OpenApiConfig {
}
