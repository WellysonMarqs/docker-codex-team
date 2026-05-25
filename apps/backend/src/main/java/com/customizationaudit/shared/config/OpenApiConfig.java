package com.customizationaudit.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    OpenAPI customizationAuditOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customization Audit API")
                        .version("0.1.0")
                        .description("API para controle e auditoria de customizacoes do sistema legado.")
                        .license(new License().name("Proprietary")))
                .schemaRequirement(BEARER_AUTH, new SecurityScheme()
                        .name(BEARER_AUTH)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
