package com.intezya.unihub.security

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequireUserType(
    vararg val value: com.intezya.unihub.domain.entity.UserType,
)
