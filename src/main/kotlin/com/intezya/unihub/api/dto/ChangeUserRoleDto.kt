package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.UserType

data class ChangeUserRoleRequest(
    val userId: String,
    val newRole: UserType,
)

data class ChangeUserRoleResponse(
    val userId: String,
    val oldRole: UserType,
    val newRole: UserType,
    val message: String,
)
