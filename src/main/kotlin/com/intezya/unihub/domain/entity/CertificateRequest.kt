package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class CertificateRequest(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: CertificateRequestStatus = CertificateRequestStatus.NEW,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

enum class CertificateRequestStatus {
    NEW,
    APPROVED,
    REJECTED,
}
