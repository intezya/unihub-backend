package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "student_profiles")
data class StudentProfile(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Column(name = "first_name", nullable = false)
    var firstName: String = "",

    @Column(name = "last_name", nullable = false)
    var lastName: String = "",

    @Column(name = "student_number", nullable = false, unique = true)
    var studentNumber: String = "",

    @Column(name = "group_name", nullable = false)
    var groupName: String = "",

    @Column(name = "direction", nullable = false)
    var direction: String = "",

    @ManyToOne
    @JoinColumn(name = "university_id")
    var university: University? = null,

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    var schedule: Schedule? = null,
)
