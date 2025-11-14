package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ApplyToProjectRequest
import com.intezya.unihub.api.dto.CreateProjectRequest
import com.intezya.unihub.api.dto.ProjectApplicationDto
import com.intezya.unihub.api.dto.ProjectDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Проекты (расширенные)", description = "API проектов с поддержкой откликов")
interface ProjectEnhancedApi {

    @Operation(
        summary = "Создать проект",
        description = "Создает новый проект/стартап от имени текущего пользователя",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Проект успешно создан"),
            ApiResponse(responseCode = "400", description = "Неверные данные"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun createProject(request: CreateProjectRequest): ProjectDto

    @Operation(
        summary = "Откликнуться на проект",
        description = "Отправляет отклик на проект с описанием опыта",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Отклик успешно отправлен"),
            ApiResponse(responseCode = "404", description = "Проект не найден"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun applyToProject(
        @Parameter(description = "ID проекта") id: Long,
        request: ApplyToProjectRequest,
    ): ProjectApplicationDto

    @Operation(
        summary = "Получить мои отклики",
        description = "Возвращает список всех откликов текущего пользователя",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Отклики успешно получены"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getMyApplications(): List<ProjectApplicationDto>

    @Operation(
        summary = "Получить отклики на мои проекты",
        description = "Возвращает список всех откликов на проекты, созданные текущим пользователем",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Отклики успешно получены"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getApplicationsForMyProjects(): List<ProjectApplicationDto>
}
