package com.intezya.unihub.security

import com.intezya.unihub.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.util.*

@Component
class AuthFilter(
    private val userService: UserService,
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val http = request as HttpServletRequest

        val serviceId = http.cookies
            ?.firstOrNull { it.name == "serviceId" }
            ?.value

        if (serviceId != null) {
            val user = userService.findByServiceId(UUID.fromString(serviceId))
            if (user != null) {
                val auth = UserAuthentication(user)
                SecurityContextHolder.getContext().authentication = auth
            }
        }

        chain.doFilter(request, response)
    }
}
