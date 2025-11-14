package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.Project
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ProjectDto(
    val id: Long,
    val title: String,
    val description: String,
    val author: String,
    val status: String,
    val category: String,
    val createdAt: String,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        fun from(
            project: Project,
            authorName: String,
            status: String = "Активен",
            category: String = "IT",
        ): ProjectDto = ProjectDto(
            id = project.id.hashCode().toLong(),
            title = project.title,
            description = project.description,
            author = authorName,
            status = status,
            category = category,
            createdAt = java.time.LocalDateTime.now().atZone(ZoneId.systemDefault()).format(formatter),
        )
    }
}
