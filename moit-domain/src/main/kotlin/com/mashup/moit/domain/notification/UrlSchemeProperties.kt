package com.mashup.moit.domain.notification

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(UrlSchemeProperties::class)
class UrlSchemePropertiesConfiguration


@ConfigurationProperties("url-scheme")
@ConstructorBinding
data class UrlSchemeProperties(
    val studyScheduled: String,
    val attendanceStart: String,
    val checkFine: String,
)
