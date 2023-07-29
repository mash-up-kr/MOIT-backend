package com.mashup.moit.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig {
    @Bean(name = ["asyncSchedulerExecutor"])
    fun asyncSchedulerExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 5
            maxPoolSize = 5
            queueCapacity = 20
            setThreadNamePrefix("AsyncSchedulerThread-")
            setWaitForTasksToCompleteOnShutdown(true)
        }
    }

    @Bean(name = ["pushSchedulerExecutor"])
    fun pushSchedulerExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 5
            maxPoolSize = 5
            queueCapacity = 20
            setThreadNamePrefix("PushSchedulerThread-")
            setWaitForTasksToCompleteOnShutdown(true)
        }
    }

    @Bean(name = ["absenceSchedulerExecutor"])
    fun absenceSchedulerExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 5
            maxPoolSize = 5
            queueCapacity = 20
            setThreadNamePrefix("AbsenceSchedulerThread-")
            setWaitForTasksToCompleteOnShutdown(true)
        }
    }
}
