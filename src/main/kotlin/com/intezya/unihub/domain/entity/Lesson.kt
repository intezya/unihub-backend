package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "lessons")
class Lesson(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    val schedule: Schedule,

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalTime,

    @Column(name = "subject", nullable = false)
    val subject: String,

    @Column(name = "teacher_name", nullable = false)
    val teacherName: String,

    @Column(name = "location", nullable = false)
    val location: String,
)
