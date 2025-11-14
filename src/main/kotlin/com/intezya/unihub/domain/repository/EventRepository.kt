package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepository : JpaRepository<Event, UUID> {
    @Query("SELECT e FROM Event e WHERE e.university.id = :universityId ORDER BY e.eventDate DESC")
    fun findByUniversityId(@Param("universityId") universityId: UUID): List<Event>
}
