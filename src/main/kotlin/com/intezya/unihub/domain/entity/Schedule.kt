package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "schedules")
class Schedule(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    // Вуз-владелец расписания
    @ManyToOne(fetch = FetchType.LAZY)
    val university: University,

    // Студенты, использующие это расписание
    @OneToMany(mappedBy = "schedule")
    val studentProfiles: MutableList<StudentProfile> = mutableListOf(),

    // Конкретные пары в рамках этого расписания
    @OneToMany(mappedBy = "schedule")
    val lessons: MutableList<Lesson> = mutableListOf(),
)
