package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "schedules")
class Schedule(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    var name: String = "",

    // Вуз-владелец расписания
    @ManyToOne(fetch = FetchType.LAZY)
    var university: University? = null,

    // Студенты, использующие это расписание
    @OneToMany(mappedBy = "schedule")
    var studentProfiles: MutableList<StudentProfile> = mutableListOf(),

    // Конкретные пары в рамках этого расписания
    @OneToMany(mappedBy = "schedule")
    var lessons: MutableList<Lesson> = mutableListOf(),
)
