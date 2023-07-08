package com.mashup.moit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class MoitApplication

fun main(args: Array<String>) {
    runApplication<MoitApplication>(*args)
}
