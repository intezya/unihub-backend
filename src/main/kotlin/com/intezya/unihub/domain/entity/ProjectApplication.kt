package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "project_applications")
data class ProjectApplication(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    var project: Project? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    var applicant: StudentProfile? = null,

    @Column(name = "applicant_name", nullable = false)
    var applicantName: String = "",

    @Column(name = "applicant_email", nullable = false)
    var applicantEmail: String = "",

    @Column(name = "experience", nullable = false, columnDefinition = "TEXT")
    var experience: String = "",

    @Column(name = "applied_at", nullable = false)
    var appliedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ProjectApplicationStatus = ProjectApplicationStatus.NEW,
)

enum class ProjectApplicationStatus {
    NEW,
    REVIEWED,
    ACCEPTED,
    REJECTED,
}
