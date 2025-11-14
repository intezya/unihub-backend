package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "internships")
class Internship(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = true)
    var university: University? = null,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "company_name", nullable = false)
    var companyName: String = "",

    @Column(name = "location")
    var location: String? = null,

    @Column(name = "start_date")
    var startDate: LocalDate? = null,

    @Column(name = "end_date")
    var endDate: LocalDate? = null,

    @Column(name = "is_paid")
    var isPaid: Boolean? = null,

    @Column(name = "logo_url")
    var logoUrl: String? = null,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: InternshipStatus = InternshipStatus.ACTIVE,

    @Column(name = "external_url")
    var externalUrl: String? = null,

    @Column(name = "direction")
    var direction: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = true)
    var creator: User? = null,
)

enum class InternshipStatus {
    ACTIVE, // Активна
    CLOSED, // Закрыта
    COMPLETED, // Завершена
}
