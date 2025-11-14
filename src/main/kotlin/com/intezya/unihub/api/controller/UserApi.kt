package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NextEventDto
import com.intezya.unihub.api.dto.UserMeDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Пользователь", description = "API управления пользователем")
interface UserApi {

    @Operation(
        summary = "Получить информацию о текущем пользователе",
        description = "Возвращает информацию об аутентифицированном пользователе",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getMe(): UserMeDto

    @Operation(
        summary = "Получить ближайшее событие пользователя",
        description = "Возвращает ближайшее запланированное событие для текущего пользователя (пара для студентов)",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ближайшее событие успешно получено"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getNextEvent(): NextEventDto?
}
