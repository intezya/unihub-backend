package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.Club

data class ClubDto(
    val id: Long,
    val name: String,
    val description: String,
    val logo: String?,
    val members: Int,
    val category: String,
) {
    companion object {
        fun from(club: Club, logoUrl: String?, members: Int = 0, category: String = "Общий"): ClubDto = ClubDto(
            id = club.id.hashCode().toLong(),
            name = club.name,
            description = club.description,
            logo = logoUrl,
            members = members,
            category = category,
        )
    }
}
