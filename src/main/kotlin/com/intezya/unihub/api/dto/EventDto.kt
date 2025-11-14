package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.Event
import java.time.format.DateTimeFormatter

data class EventDto(
    val id: Long,
    val title: String,
    val description: String,
    val image: String?,
    val date: String,
    val location: String?,
    val maxParticipants: Int?,
    val currentParticipants: Int,
    val createdAt: String,
    val createdBy: String,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        fun from(event: Event, imageUrl: String?, currentParticipants: Int, creatorName: String): EventDto = EventDto(
            id = event.id.hashCode().toLong(),
            title = event.title,
            description = event.description,
            image = imageUrl,
            date = event.eventDate.atZone(java.time.ZoneId.systemDefault()).format(formatter),
            location = event.location,
            maxParticipants = event.maxParticipants,
            currentParticipants = currentParticipants,
            createdAt = event.createdAt.atZone(java.time.ZoneId.systemDefault()).format(formatter),
            createdBy = creatorName,
        )
    }
}

data class EventRegistrationDto(
    val id: Long,
    val eventId: Long,
    val studentName: String,
    val studentGroup: String,
    val studentNumber: String,
    val registeredAt: String,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        fun from(registration: com.intezya.unihub.domain.entity.EventRegistration): EventRegistrationDto =
            EventRegistrationDto(
                id = registration.id.hashCode().toLong(),
                eventId = registration.event?.id.hashCode().toLong(),
                studentName = registration.studentName,
                studentGroup = registration.studentGroup,
                studentNumber = registration.studentNumber,
                registeredAt = registration.registeredAt.atZone(java.time.ZoneId.systemDefault()).format(formatter),
            )
    }
}

data class CreateEventRequest(
    val title: String,
    val description: String,
    val date: String,
    val location: String?,
    val maxParticipants: Int?,
)

data class RegisterForEventRequest(
    val eventId: Long,
)
