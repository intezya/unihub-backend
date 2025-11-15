package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NewsDto
import com.intezya.unihub.domain.entity.Lesson
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/student")
@RequireUserType(UserType.STUDENT, UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class StudentController(
    private val studentService: StudentService,
) : StudentApi {

    @GetMapping("/next-lesson")
    override fun getNextLesson(): NextLessonResponse {
        val studentId = studentService.getCurrentStudentId()
        val lesson: Lesson? = studentService.getNextLessonForStudent(studentId)

        if (lesson == null) {
            return NextLessonResponse(
                lesson = null,
                lessonDate = null,
                startsAt = null,
                startsInMinutes = null,
                isToday = null,
            )
        }

        val now = LocalDateTime.now()
        val today = now.toLocalDate()

        // Предполагаем, что lesson относится к ближайшей дате в рамках недели
        val lessonDate = studentService.resolveLessonDate(lesson, today)
        val startsAt = lessonDate.atTime(lesson.startTime)
        val startsInMinutes = java.time.Duration.between(now, startsAt).toMinutes()

        return NextLessonResponse(
            lesson = LessonDto.from(lesson),
            lessonDate = lessonDate,
            startsAt = startsAt,
            startsInMinutes = startsInMinutes,
            isToday = lessonDate.isEqual(today),
        )
    }

    @GetMapping("/schedule")
    override fun getSchedule(): List<com.intezya.unihub.api.dto.ScheduleDto> =
        studentService.getScheduleForStudentFormatted()

    @GetMapping("/me")
    override fun getProfile(): StudentProfileDto {
        val studentId = studentService.getCurrentStudentId()
        return studentService.getStudentProfile(studentId)
    }

    @GetMapping("/dashboard")
    override fun getDashboard(): StudentDashboardDto {
        val studentId = studentService.getCurrentStudentId()
        return studentService.getStudentDashboard(studentId)
    }
}

data class LessonDto(
    val id: UUID?,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val subject: String,
    val teacherName: String,
    val location: String,
) {
    companion object {
        fun from(lesson: Lesson): LessonDto = LessonDto(
            id = lesson.id,
            dayOfWeek = lesson.dayOfWeek.name,
            startTime = lesson.startTime.toString(),
            endTime = lesson.endTime.toString(),
            subject = lesson.subject,
            teacherName = lesson.teacherName,
            location = lesson.location,
        )
    }
}

data class NextLessonResponse(
    val lesson: LessonDto?,
    val lessonDate: LocalDate?,
    val startsAt: LocalDateTime?,
    val startsInMinutes: Long?,
    val isToday: Boolean?,
)

data class StudentProfileDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val studentNumber: String,
    val groupName: String,
    val direction: String,
    val universityId: UUID,
    val universityName: String,
    val scheduleId: UUID?,
)

data class StudentDashboardDto(
    val profile: StudentProfileDto,
    val nextLesson: NextLessonResponse,
    val recentNews: List<NewsDto>,
    val activeCertificateRequests: Int,
)
