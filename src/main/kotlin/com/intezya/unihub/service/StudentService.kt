package com.intezya.unihub.service

import com.intezya.unihub.domain.entity.Lesson
import com.intezya.unihub.domain.repository.LessonRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.security.UserAuthentication
import com.intezya.unihub.utils.error.Errors
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Service
class StudentService(
    private val studentProfileRepository: StudentProfileRepository,
    private val lessonRepository: LessonRepository,
    private val newsService: NewsService,
    private val certificateService: CertificateService,
) {
    fun getNextLessonForStudent(studentId: UUID): Lesson? {
        val student = studentProfileRepository.findById(studentId).orElseThrow {
            Errors.StudentProfile.notFound()
        }

        val schedule = student.schedule ?: return null

        val now: LocalDateTime = LocalDateTime.now()
        val today: LocalDate = now.toLocalDate()
        val currentTime: LocalTime = now.toLocalTime()
        val currentDay: DayOfWeek = now.dayOfWeek

        val todayLessons = lessonRepository.findNextLessonForScheduleOnDay(
            scheduleId = schedule.id,
            dayOfWeek = currentDay,
            time = currentTime,
        )

        if (todayLessons.isNotEmpty()) {
            return todayLessons.first()
        }

        for (offset in 1..6) {
            val targetDate: LocalDate = today.plusDays(offset.toLong())
            val targetDay: DayOfWeek = targetDate.dayOfWeek

            val lessons = lessonRepository.findLessonsForScheduleOnDay(
                scheduleId = schedule.id,
                dayOfWeek = targetDay,
            )

            if (lessons.isNotEmpty()) {
                return lessons.first()
            }
        }

        return null
    }

    fun getCurrentStudentId(): UUID {
        val auth = SecurityContextHolder.getContext().authentication as? UserAuthentication
            ?: throw IllegalStateException("No authenticated user")
        return auth.userId
    }

    fun resolveLessonDate(lesson: Lesson, today: LocalDate): LocalDate {
        val todayDow = today.dayOfWeek
        val lessonDow = lesson.dayOfWeek

        val offset = if (lessonDow >= todayDow) {
            lessonDow.ordinal - todayDow.ordinal
        } else {
            7 - (todayDow.ordinal - lessonDow.ordinal)
        }
        return today.plusDays(offset.toLong())
    }

    fun getScheduleForStudent(studentId: UUID): List<com.intezya.unihub.api.controller.LessonDto> {
        val student = studentProfileRepository.findById(studentId).orElseThrow {
            Errors.StudentProfile.notFound()
        }
        val schedule = student.schedule ?: return emptyList()

        return lessonRepository.findAll()
            .filter { it.schedule.id == schedule.id }
            .sortedWith(compareBy({ it.dayOfWeek.ordinal }, { it.startTime }))
            .map { com.intezya.unihub.api.controller.LessonDto.from(it) }
    }

    fun getStudentProfile(studentId: UUID): com.intezya.unihub.api.controller.StudentProfileDto {
        val student = studentProfileRepository.findById(studentId).orElseThrow {
            Errors.StudentProfile.notFound()
        }
        return com.intezya.unihub.api.controller.StudentProfileDto(
            id = student.id,
            firstName = student.firstName,
            lastName = student.lastName,
            studentNumber = student.studentNumber,
            groupName = student.groupName,
            direction = student.direction,
            universityId = student.university.id,
            universityName = student.university.name,
            scheduleId = student.schedule?.id,
        )
    }

    fun getStudentDashboard(studentId: UUID): com.intezya.unihub.api.controller.StudentDashboardDto {
        val student = studentProfileRepository.findById(studentId).orElseThrow {
            Errors.StudentProfile.notFound()
        }
        val profile = getStudentProfile(studentId)
        val nextLesson = getNextLessonForStudent(studentId)
        val now = LocalDateTime.now()
        val today = now.toLocalDate()

        val nextLessonResponse = if (nextLesson != null) {
            val lessonDate = resolveLessonDate(nextLesson, today)
            val startsAt = lessonDate.atTime(nextLesson.startTime)
            val startsInMinutes = java.time.Duration.between(now, startsAt).toMinutes()
            com.intezya.unihub.api.controller.NextLessonResponse(
                lesson = com.intezya.unihub.api.controller.LessonDto.from(nextLesson),
                lessonDate = lessonDate,
                startsAt = startsAt,
                startsInMinutes = startsInMinutes,
                isToday = lessonDate == today,
            )
        } else {
            com.intezya.unihub.api.controller.NextLessonResponse(null, null, null, null, null)
        }

        val recentNews = newsService.getRecentNewsForUniversity(student.university.id, 5)
        val activeCertificateRequests = certificateService.countActiveRequests(studentId)

        return com.intezya.unihub.api.controller.StudentDashboardDto(
            profile = profile,
            nextLesson = nextLessonResponse,
            recentNews = recentNews,
            activeCertificateRequests = activeCertificateRequests,
        )
    }
}
