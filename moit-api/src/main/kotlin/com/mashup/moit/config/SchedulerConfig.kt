package com.mashup.moit.config

import com.mashup.moit.scheduler.SchedulerBasePackage
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@ComponentScan(basePackageClasses = [SchedulerBasePackage::class])
class SchedulerConfig
