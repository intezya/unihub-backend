package com.intezya.unihub.service

import com.intezya.unihub.api.dto.ProjectDto
import com.intezya.unihub.domain.entity.ProjectStatus
import com.intezya.unihub.domain.repository.ProjectRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val studentProfileRepository: StudentProfileRepository,
) {

    fun getProjectsForUniversity(universityId: UUID): List<ProjectDto> =
        projectRepository.findByUniversityIdOrderByTitleAsc(universityId)
            .map { project ->
                val authorName = try {
                    val studentProfile = project.creator?.id?.let { creatorId ->
                        studentProfileRepository.findById(creatorId).orElse(null)
                    }
                    if (studentProfile != null) {
                        "${studentProfile.firstName} ${studentProfile.lastName}"
                    } else {
                        "Автор проекта"
                    }
                } catch (_: Exception) {
                    "Автор проекта"
                }

                // Маппим статус на русский
                val statusRu = when (project.status) {
                    ProjectStatus.ACTIVE -> "Активен"
                    ProjectStatus.RECRUITING -> "Набор"
                    ProjectStatus.COMPLETED -> "Завершен"
                }

                ProjectDto.from(
                    project = project,
                    authorName = authorName,
                    status = statusRu,
                    category = project.category.displayName,
                )
            }
}
