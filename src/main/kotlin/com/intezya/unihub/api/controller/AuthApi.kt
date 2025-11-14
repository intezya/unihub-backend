package com.intezya.unihub.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Аутентификация", description = "API аутентификации через MAX Bridge")
interface AuthApi {

    @Operation(
        summary = "Аутентификация через MAX Bridge",
        description = "Валидирует initData из MAX WebApp и возвращает JWT токен",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Успешная аутентификация",
                content = [Content(schema = Schema(implementation = MaxAuthResponse::class))],
            ),
            ApiResponse(responseCode = "401", description = "Неверные данные initData или ошибка аутентификации"),
        ],
    )
    fun authenticateWithMax(request: MaxAuthRequest): ResponseEntity<MaxAuthResponse>
}
