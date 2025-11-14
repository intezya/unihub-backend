package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "news")
class News(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    val university: University,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    val content: String,

    // Ключ объекта в MinIO (например, "news/{id}/image.jpg")
    @Column(name = "image_object_key")
    val imageObjectKey: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "published_at")
    val publishedAt: LocalDateTime? = null,

    @Column(name = "is_published", nullable = false)
    val isPublished: Boolean = false,
)
