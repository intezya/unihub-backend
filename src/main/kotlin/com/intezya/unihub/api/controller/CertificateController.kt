package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.CertificateDto
import com.intezya.unihub.domain.entity.CertificateType
import com.intezya.unihub.domain.repository.CertificateRequestRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.service.CertificateService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student/certificates")
class CertificateController(
    private val certificateService: CertificateService,
    private val studentProfileRepository: StudentProfileRepository,
    private val certificateRequestRepository: CertificateRequestRepository,
) : CertificateApi {

    @GetMapping
    override fun getMyCertificates(@RequestParam(required = false) studentId: UUID?): List<CertificateDto> =
        certificateRequestRepository.findAll().map { CertificateDto.from(it) }

    @PostMapping
    override fun createCertificateRequest(
        @RequestBody request: CreateCertificateRequestRequest,
        @RequestParam(required = false) studentId: UUID?,
    ): CertificateDto {
        val actualStudentId = studentId
            ?: studentProfileRepository.findAll().firstOrNull()?.id
            ?: throw IllegalStateException("No student found")
        val certificateRequest = certificateService.createCertificateRequest(
            studentId = actualStudentId,
            type = request.type,
            comment = request.comment,
        )
        return CertificateDto.from(certificateRequest)
    }
}

data class CreateCertificateRequestRequest(
    val type: CertificateType,
    val comment: String?,
)
