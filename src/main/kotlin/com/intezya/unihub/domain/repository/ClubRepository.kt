package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.Club
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClubRepository : JpaRepository<Club, UUID> {
    @EntityGraph(attributePaths = ["members", "university", "creator"])
    fun findByUniversityIdOrderByNameAsc(universityId: UUID): List<Club>
}
