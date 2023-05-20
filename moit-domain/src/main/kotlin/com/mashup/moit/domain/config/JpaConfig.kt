package com.mashup.moit.domain.config

import com.mashup.moit.domain.MoitDomainModule
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@Configuration
@EntityScan(basePackageClasses = [MoitDomainModule::class])
@EnableJpaRepositories(basePackageClasses = [MoitDomainModule::class])
class JpaConfig
