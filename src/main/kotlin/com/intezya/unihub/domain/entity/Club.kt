package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
class Club(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    var description: String = "",

    // Ключ изображения клуба в MinIO
    @Column(name = "image_object_key")
    var imageObjectKey: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    var creator: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    var university: University? = null,

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    var category: ClubCategory = ClubCategory.GENERAL,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "club_members",
        joinColumns = [JoinColumn(name = "club_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")],
    )
    var members: MutableSet<StudentProfile> = mutableSetOf(),
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
