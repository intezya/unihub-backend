package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.EventDto
import com.intezya.unihub.api.dto.EventRegistrationDto
import com.intezya.unihub.api.dto.RegisterForEventRequest
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.security.UserAuthentication
import com.intezya.unihub.service.EventService
import com.intezya.unihub.service.StudentService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/student/events")
@RequireUserType(UserType.STUDENT, UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
class EventController(
    private val eventService: EventService,
    private val studentService: StudentService,
) : EventApi {

    @GetMapping
    override fun getEvents(): List<EventDto> {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        return eventService.getEventsForUniversity(studentProfile.universityId)
    }

    @GetMapping("/{id}")
    override fun getEventById(@PathVariable id: Long): EventDto? {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        // Конвертируем Long в UUID (используем hashCode как временное решение)
        val events = eventService.getEventsForUniversity(studentProfile.universityId)
        return events.find { it.id == id }
    }

    @PostMapping("/{id}/register")
    override fun registerForEvent(
        @PathVariable id: Long,
        @RequestBody request: RegisterForEventRequest,
    ): EventRegistrationDto {
        val auth = SecurityContextHolder.getContext().authentication as UserAuthentication
        val userId = auth.userId

        val studentId = studentService.getCurrentStudentId()
        // Находим событие по ID
        val studentProfile = studentService.getStudentProfile(studentId)
        val events = eventService.getEventsForUniversity(studentProfile.universityId)
        events.find { it.id == id } ?: throw IllegalArgumentException("Event not found")

        // Регистрируем через UUID студента
        return eventService.registerForEvent(userId, studentId)
    }
}
