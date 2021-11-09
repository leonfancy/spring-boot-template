package org.chenliang.spring.template.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@OpenAPIDefinition(info = @Info(title = "Free Park API", version = "v1"))
//@SecurityScheme(
//    name = "bearer token",
//    type = SecuritySchemeType.HTTP,
//    bearerFormat = "opaque",
//    scheme = "bearer"
//)
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "Bearer Token";
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new Components()
                .addSecuritySchemes(securitySchemeName,
                                    new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                )
        )
        .info(new Info().title("Free Park API").version("v1"));
  }
}
