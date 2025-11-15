// package com.intezya.unihub.security
//
// import com.intezya.unihub.service.UserService
// import jakarta.servlet.FilterChain
// import jakarta.servlet.ServletRequest
// import jakarta.servlet.ServletResponse
// import jakarta.servlet.http.HttpServletRequest
// import org.springframework.security.core.context.SecurityContextHolder
// import org.springframework.stereotype.Component
// import org.springframework.web.filter.GenericFilterBean
//
// @Component
// class AuthFilter(
//    private val userService: UserService,
//    private val jwtService: JwtService,
// ) : GenericFilterBean() {
//
//    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
//        val http = request as HttpServletRequest
//
//        // Проверяем JWT токен из Authorization заголовка
//        val authHeader = http.getHeader("Authorization")
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            val token = authHeader.substring(7)
//            if (jwtService.validateToken(token)) {
//                val userId = jwtService.getUserIdFromToken(token)
//                if (userId != null) {
//                    val user = userService.findById(userId)
//                    if (user != null) {
//                        val auth = UserAuthentication(user)
//                        SecurityContextHolder.getContext().authentication = auth
//                    }
//                }
//            }
//        }
//
//        chain.doFilter(request, response)
//    }
// }
