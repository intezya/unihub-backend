package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "projects")
class Project(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    val id: UUID? = null,

    @JoinColumn(name = "creator_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val creator: User,

    @Column(name = "image_object_key")
    val imageObjectKey: String? = null,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "title", nullable = false)
    val title: String,

    @JoinColumn(name = "university_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val university: University,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: ProjectStatus = ProjectStatus.ACTIVE,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: ProjectCategory = ProjectCategory.IT,
)

enum class ProjectStatus {
    ACTIVE, // Активен
    RECRUITING, // Набор
    COMPLETED, // Завершен
}

enum class ProjectCategory(
    val displayName: String,
) {
    IT("IT"),
    SCIENCE("Наука"),
    BUSINESS("Бизнес"),
    DESIGN("Дизайн"),
    ENGINEERING("Инженерия"),
    MEDICINE("Медицина"),
    EDUCATION("Образование"),
    ECOLOGY("Экология"),
    SOCIAL("Социальные проекты"),
    OTHER("Другое"),
}
