package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NewsDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Новости", description = "API новостей для студентов")
interface NewsApi {

    @Operation(
        summary = "Получить список новостей",
        description = "Возвращает список последних новостей университета студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список новостей успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getNews(): List<NewsDto>

    @Operation(
        summary = "Получить новость по ID",
        description = "Возвращает конкретную новость по ID",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Новость успешно получена"),
            ApiResponse(responseCode = "404", description = "Новость не найдена"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getNewsById(@Parameter(description = "ID новости") id: Long): NewsDto?
}
