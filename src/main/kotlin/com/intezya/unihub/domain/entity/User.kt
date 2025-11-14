package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
data class User(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "service_id", nullable = false, unique = true)
    val serviceId: UUID,

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val userType: UserType,

    @Column(name = "avatar_url", nullable = true)
    val avatarUrl: String? = null,
)

enum class UserType {
    STUDENT,
    ADMIN,
    UNIVERSITY_ADMIN,
}
