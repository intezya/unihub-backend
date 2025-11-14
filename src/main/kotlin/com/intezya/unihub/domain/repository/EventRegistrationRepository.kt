package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.EventRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRegistrationRepository : JpaRepository<EventRegistration, UUID> {
    @Query("SELECT er FROM EventRegistration er WHERE er.event.id = :eventId")
    fun findByEventId(@Param("eventId") eventId: UUID): List<EventRegistration>

    @Query("SELECT COUNT(er) FROM EventRegistration er WHERE er.event.id = :eventId")
    fun countByEventId(@Param("eventId") eventId: UUID): Int

    @Query(
        "SELECT CASE WHEN COUNT(er) > 0 THEN true ELSE false END FROM EventRegistration er WHERE er.event.id = :eventId AND er.student.id = :studentId",
    )
    fun existsByEventIdAndStudentId(@Param("eventId") eventId: UUID, @Param("studentId") studentId: UUID): Boolean
}
