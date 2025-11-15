package com.intezya.unihub.service

import com.intezya.unihub.api.dto.ChangeUserRoleResponse
import com.intezya.unihub.api.dto.NextEventDto
import com.intezya.unihub.api.dto.UserMeDto
import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.domain.repository.LessonRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.domain.repository.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val studentProfileRepository: StudentProfileRepository,
    private val lessonRepository: LessonRepository,
    private val entityManager: EntityManager,
) {

    fun findByServiceId(serviceId: UUID): User? = userRepository.findByServiceId(serviceId).orElse(null)

    fun findById(userId: UUID): User? = userRepository.findById(userId).orElse(null)

    @Transactional
    fun findOrCreateByMaxId(maxUserId: Long, firstName: String?, lastName: String?, photoUrl: String?): User {
        // Ищем существующего пользователя
        val existingUser = userRepository.findByMaxUserId(maxUserId).orElse(null)
        if (existingUser != null) {
            return existingUser
        }

        // Создаем нового пользователя
        try {
            val newUser = User(
                serviceId = UUID.randomUUID(),
                userType = UserType.STUDENT,
                avatarUrl = photoUrl,
                maxUserId = maxUserId,
            )

            val savedUser = userRepository.save(newUser)
            entityManager.flush()

            // Создаем профиль студента
            val studentProfile = com.intezya.unihub.domain.entity.StudentProfile(
                id = savedUser.id,
                user = savedUser,
                firstName = firstName ?: "",
                lastName = lastName ?: "",
                studentNumber = maxUserId.toString(),
                groupName = "",
                direction = "",
            )
            studentProfileRepository.save(studentProfile)

            return savedUser
        } catch (_: DataIntegrityViolationException) {
            // Если произошла ошибка дублирования, значит пользователь был создан между проверкой и insert
            // Повторно ищем пользователя
            return userRepository.findByMaxUserId(maxUserId).orElseThrow {
                IllegalStateException("User with maxUserId=$maxUserId should exist but not found")
            }
        }
    }

    fun getUserMeByUserId(userId: UUID): UserMeDto {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }
        return buildUserMeDto(user)
    }

    private fun buildUserMeDto(user: User): UserMeDto {
        val name: String
        val role: String
        val email: String

        when (user.userType) {
            UserType.STUDENT -> {
                val profile = studentProfileRepository.findById(user.id).orElse(null)
                name = if (profile != null) {
                    "${profile.firstName} ${profile.lastName}"
                } else {
                    "Student"
                }
                role = "student"
                email = "student${user.maxUserId ?: user.id.hashCode()}@unihub.edu"
            }

            UserType.UNIVERSITY_ADMIN -> {
                name = "Admin"
                role = "staff"
                email = "admin@unihub.edu"
            }

            UserType.ADMIN -> {
                name = "Admin"
                role = "staff"
                email = "admin@unihub.edu"
            }
        }

        return UserMeDto(
            id = user.id.hashCode().toLong(),
            name = name,
            email = email,
            role = role,
            avatar = user.avatarUrl,
        )
    }

    fun getNextEventByUserId(userId: UUID): NextEventDto? {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }
        return getNextEventForUser(user)
    }

    private fun getNextEventForUser(user: User): NextEventDto? {
        // Для студента возвращаем ближайшую пару
        if (user.userType == UserType.STUDENT) {
            val profile = studentProfileRepository.findById(user.id).orElse(null) ?: return null
            val schedule = profile.schedule ?: return null

            val now = LocalTime.now()
            val today = DayOfWeek.from(LocalDate.now())

            val nextLessons = lessonRepository.findNextLessonForScheduleOnDay(
                scheduleId = schedule.id,
                dayOfWeek = today,
                time = now,
            )

            val nextLesson = nextLessons.firstOrNull() ?: return null

            return NextEventDto(
                id = nextLesson.id.toString(),
                title = nextLesson.subject,
                startTime = nextLesson.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                endTime = nextLesson.endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                location = nextLesson.location,
                type = nextLesson.lessonType.displayName,
            )
        }

        // Для других ролей можно вернуть null или реализовать другую логику
        return null
    }

    fun changeUserRole(userId: UUID, newRole: UserType): ChangeUserRoleResponse {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found with id: $userId")
        }

        val oldRole = user.userType
        user.userType = newRole
        userRepository.save(user)

        return ChangeUserRoleResponse(
            userId = userId.toString(),
            oldRole = oldRole,
            newRole = newRole,
            message = "User role changed successfully from $oldRole to $newRole",
        )
    }
}
