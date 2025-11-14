package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "image_url", nullable = true)
    var imageUrl: String? = null,

    @Column(name = "event_date", nullable = false)
    var eventDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "location", nullable = true)
    var location: String? = null,

    @Column(name = "max_participants", nullable = true)
    var maxParticipants: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    var university: University? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    var createdBy: User? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL])
    var registrations: MutableList<EventRegistration> = mutableListOf(),
)
