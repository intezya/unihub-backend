package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "certificate_requests")
class CertificateRequest(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @ManyToOne
    @JoinColumn(name = "student_profile_id", nullable = false)
    var student: StudentProfile? = null,

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    var university: University? = null,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: CertificateRequestStatus = CertificateRequestStatus.NEW,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: CertificateType = CertificateType.STUDY_CERTIFICATE,

    @Column(name = "comment")
    var comment: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "processed_at")
    var processedAt: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "processed_by_admin_id")
    var processedBy: AdminProfile? = null,

    @Column(name = "file_object_key")
    var fileObjectKey: String? = null,
) {
    fun copy(
        status: CertificateRequestStatus = this.status,
        fileObjectKey: String? = this.fileObjectKey,
        processedAt: LocalDateTime? = this.processedAt,
        processedBy: AdminProfile? = this.processedBy,
    ): CertificateRequest = CertificateRequest(
        id = this.id,
        user = this.user,
        student = this.student,
        university = this.university,
        status = status,
        type = this.type,
        comment = this.comment,
        createdAt = this.createdAt,
        processedAt = processedAt,
        processedBy = processedBy,
        fileObjectKey = fileObjectKey,
    )
}

enum class CertificateRequestStatus {
    NEW,
    APPROVED,
    REJECTED,
}

enum class CertificateType {
    STUDY_CERTIFICATE,
}
