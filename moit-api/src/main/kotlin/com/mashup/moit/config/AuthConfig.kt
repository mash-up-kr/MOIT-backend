package com.mashup.moit.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.config.AuthConfig.AuthProperty
import com.mashup.moit.security.jwt.JwtTokenSupporter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AuthProperty::class)
class AuthConfig {

    @Bean
    fun jwtTokenSupporter(authProperty: AuthProperty, mapper: ObjectMapper): JwtTokenSupporter {
        return JwtTokenSupporter(authProperty.jwtSecretKey, mapper)
    }

    @ConfigurationProperties(prefix = "moit.auth")
    data class AuthProperty(var jwtSecretKey: String)

}

