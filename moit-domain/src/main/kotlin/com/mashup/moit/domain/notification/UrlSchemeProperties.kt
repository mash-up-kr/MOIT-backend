package com.mashup.moit.domain.notification

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(UrlSchemeProperties::class)
class UrlSchemePropertiesConfiguration


@ConfigurationProperties(prefix = "url-scheme")
data class UrlSchemeProperties(
    val studyScheduled: String,
    val attendanceStart: String,
    val checkFine: String,
)
