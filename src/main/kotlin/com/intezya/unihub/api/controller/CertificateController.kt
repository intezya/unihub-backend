package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.CertificateDto
import com.intezya.unihub.domain.entity.CertificateType
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.CertificateService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/student/certificates")
@RequireUserType(UserType.STUDENT, UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class CertificateController(
    private val certificateService: CertificateService,
    private val studentService: StudentService,
) : CertificateApi {

    @GetMapping
    override fun getMyCertificates(): List<CertificateDto> {
        val studentId = studentService.getCurrentStudentId()
        return certificateService.getStudentRequests(studentId)
            .map { CertificateDto.from(it) }
    }

    @PostMapping
    override fun createCertificateRequest(@RequestBody request: CreateCertificateRequestRequest): CertificateDto {
        val studentId = studentService.getCurrentStudentId()
        val certificateRequest = certificateService.createCertificateRequest(
            studentId = studentId,
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
