package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ChangeUserRoleRequest
import com.intezya.unihub.api.dto.ChangeUserRoleResponse
import com.intezya.unihub.api.dto.NextEventDto
import com.intezya.unihub.api.dto.UserMeDto
import com.intezya.unihub.security.UserAuthentication
import com.intezya.unihub.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user", "/user")
class UserController(
    private val userService: UserService,
) : UserApi {

    @GetMapping("/me")
    override fun getMe(): UserMeDto {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is UserAuthentication) {
            return userService.getUserMeByUserId(auth.userId)
        }
        throw IllegalArgumentException("User not authenticated")
    }

    @GetMapping("/next-event")
    override fun getNextEvent(): NextEventDto? {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is UserAuthentication) {
            return userService.getNextEventByUserId(auth.userId)
        }

        throw IllegalArgumentException("User not authenticated")
    }

    @PostMapping("/change-role")
    fun changeUserRole(@RequestBody request: ChangeUserRoleRequest): ChangeUserRoleResponse {
        val userId = java.util.UUID.fromString(request.userId)
        return userService.changeUserRole(userId, request.newRole)
    }
}
