package com.intezya.unihub.service

import com.intezya.unihub.domain.entity.CertificateRequest
import com.intezya.unihub.domain.entity.CertificateRequestStatus
import com.intezya.unihub.domain.entity.CertificateType
import com.intezya.unihub.domain.repository.CertificateRequestRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.utils.error.Errors
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class CertificateService(
    private val certificateRequestRepository: CertificateRequestRepository,
    private val studentProfileRepository: StudentProfileRepository,
) {

    fun createCertificateRequest(studentId: UUID, type: CertificateType, comment: String?): CertificateRequest {
        val student = studentProfileRepository.findById(studentId).orElseThrow {
            Errors.StudentProfile.notFound()
        }

        val request = CertificateRequest(
            user = student.user,
            student = student,
            university = student.university,
            type = type,
            comment = comment,
            status = CertificateRequestStatus.NEW,
            createdAt = LocalDateTime.now(),
        )

        return certificateRequestRepository.save(request)
    }

    fun getStudentRequests(studentId: UUID): List<CertificateRequest> =
        certificateRequestRepository.findByStudentIdOrderByCreatedAtDesc(studentId)

    fun countActiveRequests(studentId: UUID): Int =
        certificateRequestRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
            .count { it.status == CertificateRequestStatus.NEW }
}
