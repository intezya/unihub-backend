package com.intezya.unihub.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Студент", description = "API информации о студенте")
interface StudentApi {

    @Operation(
        summary = "Получить ближайшую пару",
        description = "Возвращает ближайшую запланированную пару для текущего студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ближайшая пара успешно получена"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getNextLesson(): NextLessonResponse

    @Operation(
        summary = "Получить расписание",
        description = "Возвращает полное расписание для текущего студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Расписание успешно получено"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getSchedule(): List<com.intezya.unihub.api.dto.ScheduleDto>

    @Operation(
        summary = "Получить профиль студента",
        description = "Возвращает информацию о профиле текущего студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Профиль успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getProfile(): StudentProfileDto

    @Operation(
        summary = "Получить дашборд студента",
        description = "Возвращает данные дашборда включая профиль, ближайшую пару и последние новости",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Дашборд успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getDashboard(): StudentDashboardDto
}
