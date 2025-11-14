package com.intezya.unihub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class UniHubApplication

fun main(args: Array<String>) {
    runApplication<UniHubApplication>(*args)
}
