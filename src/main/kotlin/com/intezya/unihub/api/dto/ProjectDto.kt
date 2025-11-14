package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.Project
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ProjectDto(
    val id: Long,
    val title: String,
    val description: String,
    val author: String,
    val authorId: Long,
    val status: String,
    val category: String,
    val createdAt: String,
    val lookingFor: String?,
    val contactInfo: String?,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        fun from(project: Project, authorName: String, authorId: Long): ProjectDto {
            val status = when (project.status) {
                com.intezya.unihub.domain.entity.ProjectStatus.ACTIVE -> "Активен"
                com.intezya.unihub.domain.entity.ProjectStatus.RECRUITING -> "Набор"
                com.intezya.unihub.domain.entity.ProjectStatus.COMPLETED -> "Завершен"
            }

            return ProjectDto(
                id = project.id.hashCode().toLong(),
                title = project.title,
                description = project.description,
                author = authorName,
                authorId = authorId,
                status = status,
                category = project.category.displayName,
                createdAt = project.createdAt.atZone(ZoneId.systemDefault()).format(formatter),
                lookingFor = project.lookingFor,
                contactInfo = project.contactInfo,
            )
        }
    }
}

data class ProjectApplicationDto(
    val id: Long,
    val projectId: Long,
    val applicantName: String,
    val applicantEmail: String,
    val experience: String,
    val appliedAt: String,
    val status: String,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        fun from(application: com.intezya.unihub.domain.entity.ProjectApplication): ProjectApplicationDto {
            val status = when (application.status) {
                com.intezya.unihub.domain.entity.ProjectApplicationStatus.NEW -> "Новая"
                com.intezya.unihub.domain.entity.ProjectApplicationStatus.REVIEWED -> "Рассмотрена"
                com.intezya.unihub.domain.entity.ProjectApplicationStatus.ACCEPTED -> "Принята"
                com.intezya.unihub.domain.entity.ProjectApplicationStatus.REJECTED -> "Отклонена"
            }

            return ProjectApplicationDto(
                id = application.id.hashCode().toLong(),
                projectId = application.project?.id.hashCode().toLong(),
                applicantName = application.applicantName,
                applicantEmail = application.applicantEmail,
                experience = application.experience,
                appliedAt = application.appliedAt.atZone(ZoneId.systemDefault()).format(formatter),
                status = status,
            )
        }
    }
}

data class CreateProjectRequest(
    val title: String,
    val description: String,
    val category: String,
    val lookingFor: String?,
    val contactInfo: String?,
)

data class ApplyToProjectRequest(
    val projectId: Long,
    val experience: String,
)
