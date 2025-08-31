package com.madiest.moapin.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/** OpenAPI configuration for Swagger UI & JWT security. */
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Moapin API", version = "v1", description = "Moapin service REST API"),
    security = {@SecurityRequirement(name = "bearer-jwt")})
@SecurityScheme(
    name = "bearer-jwt",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class OpenApiConfig {}
