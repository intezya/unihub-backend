package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NextEventDto
import com.intezya.unihub.api.dto.UserMeDto
import com.intezya.unihub.security.UserAuthentication
import com.intezya.unihub.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/me")
    fun getMe(): UserMeDto {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is UserAuthentication) {
            return userService.getUserMeByUserId(auth.userId)
        }
        throw IllegalArgumentException("User not authenticated")
    }

    @GetMapping("/next-event")
    fun getNextEvent(): NextEventDto? {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth is UserAuthentication) {
            return userService.getNextEventByUserId(auth.userId)
        }

        throw IllegalArgumentException("User not authenticated")
    }
}
