package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.StudentProfile

data class StudentDto(
    val id: Long,
    val name: String,
    val email: String,
    val photo: String?,
    val course: Int,
    val group: String,
) {
    companion object {
        fun from(student: StudentProfile, email: String = "", photo: String? = null, course: Int = 1): StudentDto =
            StudentDto(
                id = student.id.hashCode().toLong(),
                name = "${student.firstName} ${student.lastName}",
                email = email,
                photo = photo ?: student.user?.avatarUrl,
                course = course,
                group = student.groupName,
            )
    }
}
