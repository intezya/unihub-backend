package com.intezya.unihub.security

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Aspect
@Component
class UserTypeAspect {

    @Before("@annotation(requireUserType) || @within(requireUserType)")
    fun checkRole(requireUserType: RequireUserType) {
        val auth = SecurityContextHolder.getContext().authentication
            ?: throw IllegalAccessException("Unauthorized")

        val user = auth.principal as com.intezya.unihub.domain.entity.User

        if (user.userType !in requireUserType.value) {
            throw IllegalAccessException("Forbidden: insufficient permissions")
        }
    }
}
