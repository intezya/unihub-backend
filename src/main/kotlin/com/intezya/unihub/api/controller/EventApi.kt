package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.EventDto
import com.intezya.unihub.api.dto.EventRegistrationDto
import com.intezya.unihub.api.dto.RegisterForEventRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Мероприятия", description = "API мероприятий для студентов")
interface EventApi {

    @Operation(
        summary = "Получить список мероприятий",
        description = "Возвращает список всех мероприятий университета студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список мероприятий успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getEvents(): List<EventDto>

    @Operation(
        summary = "Получить мероприятие по ID",
        description = "Возвращает информацию о конкретном мероприятии по ID",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Мероприятие успешно получено"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            ApiResponse(responseCode = "404", description = "Мероприятие не найдено"),
        ],
    )
    fun getEventById(@Parameter(description = "ID мероприятия") id: Long): EventDto?

    @Operation(
        summary = "Зарегистрироваться на мероприятие",
        description = "Регистрирует студента на мероприятие. Проверяет лимит участников и отсутствие дублей.",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Регистрация успешна"),
            ApiResponse(responseCode = "400", description = "Мероприятие заполнено или студент уже зарегистрирован"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            ApiResponse(responseCode = "404", description = "Мероприятие не найдено"),
        ],
    )
    fun registerForEvent(
        @Parameter(description = "ID мероприятия") id: Long,
        request: RegisterForEventRequest,
    ): EventRegistrationDto
}
