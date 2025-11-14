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
) {
    companion object {
        fun from(internship: Internship): InternshipDto {
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

            return InternshipDto(
                id = internship.id.hashCode().toLong(),
                company = internship.companyName,
                position = internship.title,
                description = internship.description,
                duration = duration,
                salary = salary,
                deadline = internship.endDate?.toString() ?: "Не указан",
            )
        }
    }
}
