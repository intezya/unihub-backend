package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.CertificateRequest
import com.intezya.unihub.domain.entity.CertificateRequestStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CertificateRequestRepository : JpaRepository<CertificateRequest, UUID> {
    fun findByStudentIdOrderByCreatedAtDesc(studentId: UUID): List<CertificateRequest>
    fun findByUniversityIdAndStatus(universityId: UUID, status: CertificateRequestStatus): List<CertificateRequest>
    fun findByUniversityIdOrderByCreatedAtDesc(universityId: UUID): List<CertificateRequest>
}
