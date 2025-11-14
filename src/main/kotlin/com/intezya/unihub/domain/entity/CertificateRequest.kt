package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "certificate_requests")
class CertificateRequest(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "student_profile_id", nullable = false)
    val student: StudentProfile,

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    val university: University,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: CertificateRequestStatus = CertificateRequestStatus.NEW,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: CertificateType = CertificateType.STUDY_CERTIFICATE,

    @Column(name = "comment")
    val comment: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "processed_at")
    val processedAt: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "processed_by_admin_id")
    val processedBy: AdminProfile? = null,

    @Column(name = "file_object_key")
    val fileObjectKey: String? = null,
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
