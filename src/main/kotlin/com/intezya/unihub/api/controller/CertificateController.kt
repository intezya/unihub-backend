package com.intezya.unihub.api.controller

import com.intezya.unihub.domain.entity.CertificateRequest
import com.intezya.unihub.domain.entity.CertificateType
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.CertificateService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student/certificates")
@RequireUserType(UserType.STUDENT, UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class CertificateController(
    private val certificateService: CertificateService,
    private val studentService: StudentService,
) {

    @GetMapping
    fun getMyCertificates(): List<CertificateRequestDto> {
        val studentId = studentService.getCurrentStudentId()
        return certificateService.getStudentRequests(studentId)
            .map { CertificateRequestDto.from(it) }
    }

    @PostMapping
    fun createCertificateRequest(@RequestBody request: CreateCertificateRequestRequest): CertificateRequestDto {
        val studentId = studentService.getCurrentStudentId()
        val certificateRequest = certificateService.createCertificateRequest(
            studentId = studentId,
            type = request.type,
            comment = request.comment,
        )
        return CertificateRequestDto.from(certificateRequest)
    }
}

data class CreateCertificateRequestRequest(
    val type: CertificateType,
    val comment: String?,
)

data class CertificateRequestDto(
    val id: UUID,
    val status: String,
    val type: String,
    val comment: String?,
    val createdAt: String,
    val processedAt: String?,
) {
    companion object {
        fun from(request: CertificateRequest): CertificateRequestDto = CertificateRequestDto(
            id = request.id,
            status = request.status.name,
            type = request.type.name,
            comment = request.comment,
            createdAt = request.createdAt.toString(),
            processedAt = request.processedAt?.toString(),
        )
    }
}
