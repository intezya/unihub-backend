package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Table(name = "event_registrations")
@Entity
data class EventRegistration(

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    var id: UUID? = null,

    @Column(name = "registered_at", nullable = false)
    var registeredAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "student_number", nullable = false)
    var studentNumber: String = "",

    @Column(name = "student_group", nullable = false)
    var studentGroup: String = "",

    @Column(name = "student_name", nullable = false)
    var studentName: String = "",

    @JoinColumn(name = "student_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var student: StudentProfile? = null,

    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var event: Event? = null,
)
