package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.Lesson

data class ScheduleDto(
    val id: Long,
    val subject: String,
    val time: String,
    val room: String,
    val teacher: String,
    val type: String,
    val date: String,
) {
    companion object {
        fun from(lesson: Lesson, date: String): ScheduleDto = ScheduleDto(
            id = lesson.id.hashCode().toLong(),
            subject = lesson.subject,
            time = "${lesson.startTime} - ${lesson.endTime}",
            room = lesson.location,
            teacher = lesson.teacherName,
            type = lesson.lessonType.displayName,
            date = date,
        )
    }
}
