package com.intezya.unihub.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openApi(): OpenAPI = OpenAPI().servers(
        listOf(
            Server().url("https://backend.hackathon-max.prod.stands.intezya.ru"),
            Server().url("http://localhost:8080"),
        ),
    )
}
