package com.mashup.moit.config

import com.mashup.moit.security.jwt.JwtTokenSupporter
import com.mashup.moit.util.SwaggerConstant
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openApi(): OpenAPI {
        val info = Info().title(SwaggerConstant.API_NAME)
            .version(SwaggerConstant.API_VERSION)
            .description(SwaggerConstant.API_DESCRIPTION)

        return OpenAPI()
            .addServersItem(Server().url("/moit"))
            .info(info)
            .addSecurityItem(SecurityRequirement().addList(SwaggerConstant.BEARER_AUTH))
            .components(
                Components()
                    .addSecuritySchemes(
                        SwaggerConstant.BEARER_AUTH,
                        SecurityScheme()
                            .name(SwaggerConstant.BEARER_AUTH)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme(JwtTokenSupporter.BEARER_TOKEN_PREFIX)
                            .bearerFormat("JWT")
                    )
            )
    }
}
