package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByServiceId(serviceId: UUID): Optional<User>
    fun findByMaxUserId(maxUserId: Long): Optional<User>

    fun findAllByUserType(userType: UserType, pageable: Pageable): Page<User>
    fun findAllByActive(active: Boolean, pageable: Pageable): Page<User>
    fun findAllByUserTypeAndActive(userType: UserType, active: Boolean, pageable: Pageable): Page<User>

    fun findAllByMaxUserId(maxUserId: Long, pageable: Pageable): Page<User>
    fun findAllByMaxUserIdAndUserType(maxUserId: Long, userType: UserType, pageable: Pageable): Page<User>

    fun countByUserType(userType: UserType): Long
}
