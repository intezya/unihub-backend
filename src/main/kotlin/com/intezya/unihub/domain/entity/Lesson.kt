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
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    var schedule: Schedule? = null,

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    var dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalTime = LocalTime.now(),

    @Column(name = "end_time", nullable = false)
    var endTime: LocalTime = LocalTime.now(),

    @Column(name = "subject", nullable = false)
    var subject: String = "",

    @Column(name = "teacher_name", nullable = false)
    var teacherName: String = "",

    @Column(name = "location", nullable = false)
    var location: String = "",

    @Column(name = "lesson_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var lessonType: LessonType = LessonType.LECTURE,
)

enum class LessonType(
    val displayName: String,
) {
    LECTURE("Лекция"),
    PRACTICE("Практика"),
    SEMINAR("Семинар"),
}
