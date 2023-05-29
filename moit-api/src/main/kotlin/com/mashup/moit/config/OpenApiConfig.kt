package com.mashup.moit.config

import com.mashup.moit.util.SwaggerConstant
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val info = Info().title(SwaggerConstant.API_NAME)
            .version(SwaggerConstant.API_VERSION)
            .description(SwaggerConstant.API_DESCRIPTION)

        return OpenAPI()
            .components(Components())
            .info(info)
    }
}
