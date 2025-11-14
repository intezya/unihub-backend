package com.intezya.unihub.api.controller

import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.InternshipParserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/internships")
@RequireUserType(UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
@Tag(name = "Админ - Стажировки", description = "API управления стажировками для администраторов")
class AdminInternshipController(
    private val internshipParserService: InternshipParserService,
) {

    @PostMapping("/parse")
    @Operation(
        summary = "Запустить парсинг стажировок",
        description = "Запускает парсинг стажировок с сайта postypashki.ru и обновляет базу данных",
        security = [SecurityRequirement(name = "bearerAuth")],
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Парсинг успешно запущен"),
            ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            ApiResponse(responseCode = "403", description = "Недостаточно прав"),
        ],
    )
    fun triggerInternshipsParsing(): Map<String, String> {
        internshipParserService.manualParse()
        return mapOf("status" to "success", "message" to "Парсинг стажировок запущен")
    }
}
