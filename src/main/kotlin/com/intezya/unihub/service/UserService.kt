package com.intezya.unihub.service

import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun register(userType: UserType, avatarUrl: String?): User {
        val serviceId = UUID.randomUUID()
        val user = User(
            serviceId = serviceId,
            userType = userType,
            avatarUrl = avatarUrl,
        )

        return userRepository.save(user)
    }

    fun login(serviceId: UUID): User = userRepository.findByServiceId(serviceId).orElseThrow {
        IllegalArgumentException("User not found")
    }

    fun findByServiceId(serviceId: UUID): User? = userRepository.findByServiceId(serviceId).orElse(null)
}
