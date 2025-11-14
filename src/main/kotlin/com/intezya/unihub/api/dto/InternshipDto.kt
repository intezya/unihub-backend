package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.Internship

data class InternshipDto(
    val id: Long,
    val company: String,
    val position: String,
    val description: String,
    val duration: String,
    val salary: String?,
    val deadline: String,
    val logo: String?,
    val status: String,
    val externalUrl: String?,
    val direction: String?,
) {
    companion object {
        fun from(internship: Internship, logoUrl: String?): InternshipDto {
            val duration = if (internship.startDate != null && internship.endDate != null) {
                val months = java.time.Period.between(internship.startDate, internship.endDate).toTotalMonths()
                "$months месяцев"
            } else {
                "Не указано"
            }

            val salary = if (internship.isPaid == true) {
                "По договоренности"
            } else {
                null
            }

            val status = when (internship.status) {
                com.intezya.unihub.domain.entity.InternshipStatus.ACTIVE -> "Активна"
                com.intezya.unihub.domain.entity.InternshipStatus.CLOSED -> "Закрыта"
                com.intezya.unihub.domain.entity.InternshipStatus.COMPLETED -> "Завершена"
            }

            return InternshipDto(
                id = internship.id.hashCode().toLong(),
                company = internship.companyName,
                position = internship.title,
                description = internship.description,
                duration = duration,
                salary = salary,
                deadline = internship.endDate?.toString() ?: "Не указан",
                logo = logoUrl,
                status = status,
                externalUrl = internship.externalUrl,
                direction = internship.direction,
            )
        }
    }
}
