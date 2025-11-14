package com.intezya.unihub.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import java.util.UUID

@Entity
data class StudentProfile(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "student_number", nullable = false, unique = true)
    val studentNumber: String,

    @Column(name = "group_name", nullable = false)
    val groupName: String,

    @Column(name = "direction", nullable = false)
    val direction: String,

    @ManyToOne
    @JoinColumn(name = "university_id")
    val university: University,

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    val schedule: Schedule? = null,
)
