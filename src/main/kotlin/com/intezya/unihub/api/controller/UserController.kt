package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NextEventDto
import com.intezya.unihub.api.dto.UserMeDto
import com.intezya.unihub.service.UserService
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/me")
    fun getMe(@CookieValue("serviceId") serviceId: String): UserMeDto =
        userService.getUserMe(UUID.fromString(serviceId))

    @GetMapping("/next-event")
    fun getNextEvent(@CookieValue("serviceId") serviceId: String): NextEventDto? =
        userService.getNextEvent(UUID.fromString(serviceId))
}
