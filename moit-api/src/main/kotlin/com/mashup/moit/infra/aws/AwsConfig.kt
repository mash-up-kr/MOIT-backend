package com.mashup.moit.infra.aws

import org.springframework.cloud.aws.autoconfigure.context.ContextCredentialsAutoConfiguration
import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ContextCredentialsAutoConfiguration::class, ContextRegionProviderAutoConfiguration::class)
class AwsConfig
