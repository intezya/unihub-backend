package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "projects")
class Project(
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    var id: UUID? = null,

    @JoinColumn(name = "creator_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var creator: User? = null,

    @Column(name = "image_object_key")
    var imageObjectKey: String? = null,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "title", nullable = false)
    var title: String = "",

    @JoinColumn(name = "university_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var university: University? = null,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ProjectStatus = ProjectStatus.ACTIVE,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    var category: ProjectCategory = ProjectCategory.IT,

    @Column(name = "looking_for", columnDefinition = "TEXT")
    var lookingFor: String? = null,

    @Column(name = "contact_info")
    var contactInfo: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: java.time.LocalDateTime = java.time.LocalDateTime.now(),

    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL])
    var applications: MutableList<ProjectApplication> = mutableListOf(),
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
