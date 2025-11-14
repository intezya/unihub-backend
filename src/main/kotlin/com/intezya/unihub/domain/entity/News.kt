package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "news")
class News(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    var university: University? = null,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String = "",

    // Ключ объекта в MinIO (например, "news/{id}/image.jpg")
    @Column(name = "image_object_key")
    var imageObjectKey: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "published_at")
    var publishedAt: LocalDateTime? = null,

    @Column(name = "is_published", nullable = false)
    var isPublished: Boolean = false,
)
