package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "service_id", nullable = false, unique = true)
    var serviceId: UUID = UUID.randomUUID(),

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var userType: UserType = UserType.STUDENT,

    @Column(name = "avatar_url", nullable = true)
    var avatarUrl: String? = null,

    @Column(name = "max_user_id", nullable = true, unique = true)
    var maxUserId: Long? = null,
)

enum class UserType {
    STUDENT,
    UNIVERSITY_ADMIN,
    ADMIN,
}
