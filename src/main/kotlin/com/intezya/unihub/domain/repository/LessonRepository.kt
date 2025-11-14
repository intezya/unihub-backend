package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.Lesson
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

interface LessonRepository : JpaRepository<Lesson, UUID> {

    @EntityGraph(attributePaths = ["schedule"])
    @Query(
        """
        SELECT l FROM Lesson l
        WHERE l.schedule.id = :scheduleId
          AND l.dayOfWeek = :dayOfWeek
          AND l.startTime > :time
        ORDER BY l.startTime ASC
    """,
    )
    fun findNextLessonForScheduleOnDay(
        @Param("scheduleId") scheduleId: UUID,
        @Param("dayOfWeek") dayOfWeek: DayOfWeek,
        @Param("time") time: LocalTime,
    ): List<Lesson>

    @EntityGraph(attributePaths = ["schedule"])
    @Query(
        """
        SELECT l FROM Lesson l
        WHERE l.schedule.id = :scheduleId
          AND l.dayOfWeek = :dayOfWeek
        ORDER BY l.startTime ASC
    """,
    )
    fun findLessonsForScheduleOnDay(
        @Param("scheduleId") scheduleId: UUID,
        @Param("dayOfWeek") dayOfWeek: DayOfWeek,
    ): List<Lesson>
}
