package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ClubDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.*

@Tag(name = "Клубы", description = "API студенческих клубов")
interface ClubApi {

    @Operation(
        summary = "Получить список клубов",
        description = "Возвращает список клубов университета",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список клубов успешно получен"),
        ],
    )
    fun getClubs(@Parameter(description = "ID университета (опционально)") universityId: UUID?): List<ClubDto>

    @Operation(
        summary = "Получить клуб по ID",
        description = "Возвращает информацию о конкретном клубе по ID",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Клуб успешно получен"),
            ApiResponse(responseCode = "404", description = "Клуб не найден"),
        ],
    )
    fun getClubById(
        @Parameter(description = "ID клуба") id: Long,
        @Parameter(description = "ID университета (опционально)") universityId: UUID?,
    ): ClubDto?
}
