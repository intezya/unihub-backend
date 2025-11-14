package com.intezya.unihub.service

import com.intezya.unihub.api.dto.NextEventDto
import com.intezya.unihub.api.dto.UserMeDto
import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.domain.repository.LessonRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.domain.repository.UserRepository
import org.springframework.stereotype.Service
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

    fun getUserMe(serviceId: UUID): UserMeDto {
        val user = userRepository.findByServiceId(serviceId).orElseThrow {
            IllegalArgumentException("User not found")
        }

        val name: String
        val role: String

        when (user.userType) {
            UserType.STUDENT -> {
                val profile = studentProfileRepository.findById(user.id).orElse(null)
                name = if (profile != null) {
                    "${profile.firstName} ${profile.lastName}"
                } else {
                    "Student"
                }
                role = "student"
            }

            UserType.UNIVERSITY_ADMIN -> {
                name = "Admin"
                role = "staff"
            }

            UserType.ADMIN -> {
                name = "Admin"
                role = "staff"
            }
        }

        return UserMeDto(
            id = user.id.hashCode().toLong(),
            name = name,
            role = role,
            avatar = user.avatarUrl,
        )
    }

    fun getNextEvent(serviceId: UUID): NextEventDto? {
        val user = userRepository.findByServiceId(serviceId).orElseThrow {
            IllegalArgumentException("User not found")
        }

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
}
