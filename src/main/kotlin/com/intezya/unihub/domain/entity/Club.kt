package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
class Club(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    // Ключ изображения клуба в MinIO
    @Column(name = "image_object_key")
    val imageObjectKey: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    val university: University,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    val category: ClubCategory = ClubCategory.GENERAL,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "club_members",
        joinColumns = [JoinColumn(name = "club_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")],
    )
    val members: MutableSet<StudentProfile> = mutableSetOf(),
)

enum class ClubCategory(
    val displayName: String,
) {
    GENERAL("Общий"),
    SPORT("Спорт"),
    TECHNOLOGY("Технологии"),
    SCIENCE("Наука"),
    ART("Искусство"),
    MUSIC("Музыка"),
    VOLUNTEERING("Волонтёрство"),
    BUSINESS("Бизнес"),
    LANGUAGES("Языки"),
    GAMES("Игры"),
}
