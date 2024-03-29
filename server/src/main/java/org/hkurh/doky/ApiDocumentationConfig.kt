package org.hkurh.doky

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
@OpenAPIDefinition(info = Info(title = "Doky API", version = "v1"))
@SecurityScheme(name = "Bearer Token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
class ApiDocumentationConfig
