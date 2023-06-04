package com.mashup.moit.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.moit.security.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {
    
    @Bean
    fun jwtTokenProvider(mapper: ObjectMapper): JwtTokenProvider {
        return JwtTokenProvider("secret-key-temporary-for-test-123", mapper) // TODO: Secret key 정의 및 주입 필요
    }
    
}
