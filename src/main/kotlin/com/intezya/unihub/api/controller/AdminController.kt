package com.intezya.unihub.api.controller

import com.intezya.unihub.domain.entity.CertificateRequestStatus
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.domain.repository.*
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.utils.error.Errors
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/admin")
@RequireUserType(UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class AdminController(
    private val lessonRepository: LessonRepository,
    private val newsRepository: NewsRepository,
    private val certificateRequestRepository: CertificateRequestRepository,
    private val clubRepository: ClubRepository,
    private val scheduleRepository: ScheduleRepository,
) {

    @GetMapping("/certificates")
    fun getCertificateRequests(
        @RequestParam universityId: UUID,
        @RequestParam(required = false) status: CertificateRequestStatus?,
    ): List<CertificateRequestDto> {
        val requests = if (status != null) {
            certificateRequestRepository.findByUniversityIdAndStatus(universityId, status)
        } else {
            certificateRequestRepository.findByUniversityIdOrderByCreatedAtDesc(universityId)
        }
        return requests.map { CertificateRequestDto.from(it) }
    }

    @PutMapping("/certificates/{id}")
    fun updateCertificateRequest(
        @PathVariable id: UUID,
        @RequestBody request: UpdateCertificateRequestRequest,
    ): CertificateRequestDto {
        val certificateRequest = certificateRequestRepository.findById(id).orElseThrow {
            Errors.CertificateRequest.notFound()
        }

        val updated = certificateRequest.copy(
            status = request.status ?: certificateRequest.status,
            fileObjectKey = request.fileObjectKey ?: certificateRequest.fileObjectKey,
        )

        return CertificateRequestDto.from(certificateRequestRepository.save(updated))
    }
}

data class UpdateCertificateRequestRequest(
    val status: CertificateRequestStatus?,
    val fileObjectKey: String?,
)
