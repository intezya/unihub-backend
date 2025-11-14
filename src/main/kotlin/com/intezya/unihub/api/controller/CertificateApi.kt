package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.CertificateDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Справки", description = "API справок для студентов")
interface CertificateApi {

    @Operation(
        summary = "Получить мои справки",
        description = "Возвращает список заявок на справки текущего студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список справок успешно получен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
        ],
    )
    fun getMyCertificates(): List<CertificateDto>

    @Operation(
        summary = "Создать заявку на справку",
        description = "Создает новую заявку на справку для текущего студента",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Заявка на справку успешно создана"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            ApiResponse(responseCode = "400", description = "Неверный запрос"),
        ],
    )
    fun createCertificateRequest(request: CreateCertificateRequestRequest): CertificateDto
}
