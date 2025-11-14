package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.InternshipDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Стажировки", description = "API стажировок для студентов")
interface InternshipApi {

    @Operation(
        summary = "Получить список стажировок",
        description = "Возвращает список доступных стажировок для университета студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список стажировок успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getInternships(): List<InternshipDto>
}
