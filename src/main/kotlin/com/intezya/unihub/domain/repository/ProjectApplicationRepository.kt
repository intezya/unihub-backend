package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.ProjectApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectApplicationRepository : JpaRepository<ProjectApplication, UUID> {
    @Query("SELECT pa FROM ProjectApplication pa WHERE pa.project.id = :projectId ORDER BY pa.appliedAt DESC")
    fun findByProjectId(@Param("projectId") projectId: UUID): List<ProjectApplication>

    @Query("SELECT pa FROM ProjectApplication pa WHERE pa.project.creator.id = :creatorId ORDER BY pa.appliedAt DESC")
    fun findByCreatorId(@Param("creatorId") creatorId: UUID): List<ProjectApplication>
}
