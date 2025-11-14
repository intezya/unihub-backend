package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
data class Club(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "image_url")
    val imageUrl: String? = null,

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    val creator: User,
)
