package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ProjectDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Проекты", description = "API студенческих проектов")
interface ProjectApi {

    @Operation(
        summary = "Получить список проектов",
        description = "Возвращает список проектов университета студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список проектов успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getProjects(): List<ProjectDto>

    @Operation(
        summary = "Получить проект по ID",
        description = "Возвращает информацию о конкретном проекте по ID",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Проект успешно получен"),
            ApiResponse(responseCode = "404", description = "Проект не найден"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getProjectById(@Parameter(description = "ID проекта") id: Long): ProjectDto?
}
