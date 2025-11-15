package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.CertificateDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.*

@Tag(name = "Справки", description = "API справок для студентов")
interface CertificateApi {

    @Operation(
        summary = "Получить мои справки",
        description = "Возвращает список заявок на справки студента",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Список справок успешно получен"),
        ],
    )
    fun getMyCertificates(@Parameter(description = "ID студента (опционально)") studentId: UUID?): List<CertificateDto>

    @Operation(
        summary = "Создать заявку на справку",
        description = "Создает новую заявку на справку для студента",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Заявка на справку успешно создана"),
            ApiResponse(responseCode = "400", description = "Неверный запрос"),
        ],
    )
    fun createCertificateRequest(
        request: CreateCertificateRequestRequest,
        @Parameter(description = "ID студента (опционально)") studentId: UUID?,
    ): CertificateDto
}
