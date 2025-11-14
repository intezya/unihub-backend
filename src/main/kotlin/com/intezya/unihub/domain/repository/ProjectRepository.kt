package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.Project
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<Project, UUID> {
    @EntityGraph(attributePaths = ["creator", "university"])
    fun findByUniversityIdOrderByTitleAsc(universityId: UUID): List<Project>
}
