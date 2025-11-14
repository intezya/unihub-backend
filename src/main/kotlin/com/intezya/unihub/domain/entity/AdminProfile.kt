package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "admin_profiles")
data class AdminProfile(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "university_id")
    val university: University,
)
