package com.intezya.unihub.service

import com.intezya.unihub.domain.repository.ProjectRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val avatarUrlService: AvatarUrlService,
) {

    fun getProjectsForUniversity(universityId: UUID): List<ProjectDto> =
        projectRepository.findByUniversityIdOrderByTitleAsc(universityId)
            .map { project ->
                ProjectDto(
                    id = project.id!!,
                    title = project.title,
                    description = project.description,
                    imageUrl = project.imageObjectKey?.let { avatarUrlService.generatePresignedUrl(it) },
                )
            }
}

data class ProjectDto(
    val id: UUID,
    val title: String,
    val description: String,
    val imageUrl: String?,
)
