package com.intezya.unihub.security

import com.intezya.unihub.domain.entity.User
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*

class UserAuthentication(
    private val user: User,
) : Authentication {

    private var authenticated = true

    val userId: UUID get() = user.id

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(GrantedAuthority { user.userType.name })

    override fun getCredentials(): Any = user.serviceId
    override fun getDetails(): Any = user
    override fun getPrincipal(): Any = user
    override fun isAuthenticated(): Boolean = authenticated
    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    override fun getName(): String = user.id.toString()
}
