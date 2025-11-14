package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.Internship
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InternshipRepository : JpaRepository<Internship, UUID> {
    fun findByUniversityIdOrderByTitleAsc(universityId: UUID): List<Internship>
}
