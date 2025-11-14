package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "internships")
class Internship(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    val university: University,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "company_name", nullable = false)
    val companyName: String,

    @Column(name = "location")
    val location: String? = null,

    @Column(name = "start_date")
    val startDate: LocalDate? = null,

    @Column(name = "end_date")
    val endDate: LocalDate? = null,

    @Column(name = "is_paid")
    val isPaid: Boolean? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User,
)
