package com.intezya.unihub.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*

interface LessonRepository : JpaRepository<Lesson, UUID> {

    @Query(
        """
        SELECT l FROM Lesson l
        WHERE l.schedule.id = :scheduleId
          AND l.startTime > :now
        ORDER BY l.startTime ASC
    """,
    )
    fun findNextLessonForSchedule(@Param("scheduleId") scheduleId: UUID, @Param("now") now: LocalDateTime): Lesson?
}
