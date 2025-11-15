package com.intezya.unihub.security

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class UserTypeAspect {

    @Before("@annotation(requireUserType) || @within(requireUserType)")
    fun checkRole(requireUserType: RequireUserType) {
    }
}
