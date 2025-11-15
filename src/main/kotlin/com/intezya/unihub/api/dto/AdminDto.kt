package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import java.util.*

data class AdminUserDto(
    val id: UUID,
    val serviceId: UUID,
    val userType: UserType,
    val avatarUrl: String?,
    val maxUserId: Long?,
    val active: Boolean,
) {
    companion object {
        fun from(u: User) = AdminUserDto(
            id = u.id,
            serviceId = u.serviceId,
            userType = u.userType,
            avatarUrl = u.avatarUrl,
            maxUserId = u.maxUserId,
            active = u.active,
        )
    }
}

data class ChangeRoleAdminRequest(
    val newRole: UserType,
)
