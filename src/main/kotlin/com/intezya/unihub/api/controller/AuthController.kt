package com.intezya.unihub.api.controller

import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import java.util.*

data class RegisterRequest(
    val userType: UserType,
    val avatarUrl: String? = null,
)

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
) {

    @PostMapping("/register")
    fun register(@RequestBody body: RegisterRequest, response: HttpServletResponse): User {
        val newUser = userService.register(body.userType, body.avatarUrl)

        val cookie = Cookie("serviceId", newUser.serviceId.toString())
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = 60 * 60 * 24 * 30

        response.addCookie(cookie)
        return newUser
    }

    @PostMapping("/login")
    fun login(@CookieValue("serviceId") serviceId: String): User = userService.login(UUID.fromString(serviceId))
}
