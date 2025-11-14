package com.intezya.unihub.api.dto

data class UserMeDto(
    val id: Long,
    val name: String,
    val role: String, // 'student' | 'teacher' | 'staff'
    val avatar: String?,
)

data class NextEventDto(
    val id: String,
    val title: String,
    val startTime: String,
    val endTime: String,
    val location: String,
    val type: String,
)
