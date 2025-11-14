package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ClubDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Клубы", description = "API студенческих клубов")
interface ClubApi {

    @Operation(
        summary = "Получить список клубов",
        description = "Возвращает список клубов университета студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список клубов успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getClubs(): List<ClubDto>

    @Operation(
        summary = "Получить клуб по ID",
        description = "Возвращает информацию о конкретном клубе по ID",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Клуб успешно получен"),
            ApiResponse(responseCode = "404", description = "Клуб не найден"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getClubById(@Parameter(description = "ID клуба") id: Long): ClubDto?
}
