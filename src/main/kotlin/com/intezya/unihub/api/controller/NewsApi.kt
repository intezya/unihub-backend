package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NewsDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.*

@Tag(name = "Новости", description = "API новостей для студентов")
interface NewsApi {

    @Operation(
        summary = "Получить список новостей",
        description = "Возвращает список последних новостей университета",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список новостей успешно получен"),
        ],
    )
    fun getNews(@Parameter(description = "ID университета (опционально)") universityId: UUID?): List<NewsDto>

    @Operation(
        summary = "Получить новость по ID",
        description = "Возвращает конкретную новость по ID",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Новость успешно получена"),
            ApiResponse(responseCode = "404", description = "Новость не найдена"),
        ],
    )
    fun getNewsById(
        @Parameter(description = "ID новости") id: Long,
        @Parameter(description = "ID университета (опционально)") universityId: UUID?,
    ): NewsDto?
}
