package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ScheduleRepository : JpaRepository<Schedule, UUID> {
    fun findByUniversityId(universityId: UUID): List<Schedule>
}
