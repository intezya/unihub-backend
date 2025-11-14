package com.intezya.unihub.service

import com.intezya.unihub.api.dto.CreateEventRequest
import com.intezya.unihub.api.dto.EventDto
import com.intezya.unihub.api.dto.EventRegistrationDto
import com.intezya.unihub.domain.entity.Event
import com.intezya.unihub.domain.entity.EventRegistration
import com.intezya.unihub.domain.repository.EventRegistrationRepository
import com.intezya.unihub.domain.repository.EventRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.domain.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventRegistrationRepository: EventRegistrationRepository,
    private val studentProfileRepository: StudentProfileRepository,
    private val userRepository: UserRepository,
    private val avatarUrlService: AvatarUrlService,
) {

    fun getEventsForUniversity(universityId: UUID): List<EventDto> {
        val events = eventRepository.findByUniversityId(universityId)
        return events.map { event ->
            val currentParticipants = eventRegistrationRepository.countByEventId(event.id!!)
            val creatorName = event.createdBy?.let { user ->
                val profile = studentProfileRepository.findById(user.id).orElse(null)
                if (profile != null) {
                    "${profile.firstName} ${profile.lastName}"
                } else {
                    "Администрация"
                }
            } ?: "Администрация"

            val imageUrl = event.imageUrl?.let { avatarUrlService.generatePresignedUrl(it) }

            EventDto.from(event, imageUrl, currentParticipants, creatorName)
        }
    }

    fun getEventById(eventId: UUID): EventDto? {
        val event = eventRepository.findById(eventId).orElse(null) ?: return null
        val currentParticipants = eventRegistrationRepository.countByEventId(eventId)
        val creatorName = event.createdBy?.let { user ->
            val profile = studentProfileRepository.findById(user.id).orElse(null)
            if (profile != null) {
                "${profile.firstName} ${profile.lastName}"
            } else {
                "Администрация"
            }
        } ?: "Администрация"

        val imageUrl = event.imageUrl?.let { avatarUrlService.generatePresignedUrl(it) }

        return EventDto.from(event, imageUrl, currentParticipants, creatorName)
    }

    fun createEvent(request: CreateEventRequest, creatorId: UUID, universityId: UUID): EventDto {
        val creator = userRepository.findById(creatorId).orElseThrow {
            IllegalArgumentException("User not found")
        }

        val event = Event(
            title = request.title,
            description = request.description,
            eventDate = LocalDateTime.parse(request.date),
            location = request.location,
            maxParticipants = request.maxParticipants,
            createdBy = creator,
            university = creator.let {
                val profile = studentProfileRepository.findById(it.id).orElse(null)
                profile?.university
            },
        )

        val savedEvent = eventRepository.save(event)
        return EventDto.from(savedEvent, null, 0, "Администрация")
    }

    fun registerForEvent(eventId: UUID, studentId: UUID): EventRegistrationDto {
        val event = eventRepository.findById(eventId).orElseThrow {
            IllegalArgumentException("Event not found")
        }

        val student = studentProfileRepository.findById(studentId).orElseThrow {
            IllegalArgumentException("Student not found")
        }

        // Проверка, не зарегистрирован ли уже студент
        if (eventRegistrationRepository.existsByEventIdAndStudentId(eventId, studentId)) {
            throw IllegalStateException("Student already registered for this event")
        }

        // Проверка лимита участников
        if (event.maxParticipants != null) {
            val currentCount = eventRegistrationRepository.countByEventId(eventId)
            if (currentCount >= event.maxParticipants!!) {
                throw IllegalStateException("Event is full")
            }
        }

        val registration = EventRegistration(
            event = event,
            student = student,
            studentName = "${student.firstName} ${student.lastName}",
            studentGroup = student.groupName,
            studentNumber = student.studentNumber,
        )

        val savedRegistration = eventRegistrationRepository.save(registration)
        return EventRegistrationDto.from(savedRegistration)
    }

    fun getEventRegistrations(eventId: UUID): List<EventRegistrationDto> {
        val registrations = eventRegistrationRepository.findByEventId(eventId)
        return registrations.map { EventRegistrationDto.from(it) }
    }
}
