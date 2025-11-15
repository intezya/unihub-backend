package com.intezya.unihub.service

import com.intezya.unihub.api.dto.AdminUserDto
import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.domain.repository.UserRepository
import com.intezya.unihub.utils.error.Errors
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminService(
    private val userRepository: UserRepository,
) {

    fun listUsers(page: Int, size: Int, role: UserType?, active: Boolean?): Page<AdminUserDto> {
        val pageable: Pageable = PageRequest.of(page, size)
        val pageResult: Page<User> = when {
            role != null && active != null -> userRepository.findAllByUserTypeAndActive(role, active, pageable)
            role != null -> userRepository.findAllByUserType(role, pageable)
            active != null -> userRepository.findAllByActive(active, pageable)
            else -> userRepository.findAll(pageable)
        }
        return pageResult.map { AdminUserDto.from(it) }
    }

    // поиск по maxUserId и/или роли
    fun searchUsers(maxUserId: Long?, role: UserType?, page: Int, size: Int): Page<AdminUserDto> {
        val pageable: Pageable = PageRequest.of(page, size)
        val pageResult: Page<User> = when {
            maxUserId != null && role != null -> userRepository.findAllByMaxUserIdAndUserType(maxUserId, role, pageable)
            maxUserId != null -> userRepository.findAllByMaxUserId(maxUserId, pageable)
            role != null -> userRepository.findAllByUserType(role, pageable)
            else -> userRepository.findAll(pageable)
        }
        return pageResult.map { AdminUserDto.from(it) }
    }

    fun getUserById(id: UUID): AdminUserDto {
        val user = userRepository.findById(id).orElseThrow { Errors.User.notFound() }
        return AdminUserDto.from(user)
    }

    @Transactional
    fun setActive(id: UUID, active: Boolean): AdminUserDto {
        val user = userRepository.findById(id).orElseThrow { Errors.User.notFound() }
        val updated = user.copy(active = active)
        return AdminUserDto.from(userRepository.save(updated))
    }

    @Transactional
    fun changeRole(id: UUID, newRole: UserType): AdminUserDto {
        val user = userRepository.findById(id).orElseThrow { Errors.User.notFound() }
        val updated = user.copy(userType = newRole)
        return AdminUserDto.from(userRepository.save(updated))
    }

    // продуктовая статистика по пользователям
    fun userStats(): Map<String, Any> {
        val total = userRepository.count()
        val students = userRepository.countByUserType(UserType.STUDENT)
        val admins = userRepository.countByUserType(UserType.ADMIN)
        val uniAdmins = userRepository.countByUserType(UserType.UNIVERSITY_ADMIN)

        return mapOf(
            "total" to total,
            "students" to students,
            "admins" to admins,
            "universityAdmins" to uniAdmins,
        )
    }

    // Placeholder for reindexing  should call real search service
    fun reindex(): String = "reindex-triggered"

    fun systemStats(): Map<String, Any> {
        val runtime = Runtime.getRuntime()
        return mapOf(
            "availableProcessors" to runtime.availableProcessors(),
            "freeMemory" to runtime.freeMemory(),
            "totalMemory" to runtime.totalMemory(),
            "maxMemory" to runtime.maxMemory(),
        )
    }

    fun runMigrations(): String = "migrations-triggered"
}
