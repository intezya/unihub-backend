package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.CertificateRequest
import com.intezya.unihub.domain.entity.CertificateRequestStatus
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CertificateRequestRepository : JpaRepository<CertificateRequest, UUID> {
    @EntityGraph(attributePaths = ["user", "student", "university", "processedBy"])
    fun findByStudentIdOrderByCreatedAtDesc(studentId: UUID): List<CertificateRequest>

    @EntityGraph(attributePaths = ["user", "student", "university", "processedBy"])
    fun findByUniversityIdAndStatus(universityId: UUID, status: CertificateRequestStatus): List<CertificateRequest>

    @EntityGraph(attributePaths = ["user", "student", "university", "processedBy"])
    fun findByUniversityIdOrderByCreatedAtDesc(universityId: UUID): List<CertificateRequest>

    @EntityGraph(attributePaths = ["user", "student", "university", "processedBy"])
    override fun findById(id: UUID): Optional<CertificateRequest>
}
