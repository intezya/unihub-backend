package com.intezya.unihub.domain.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "admin_profiles")
data class AdminProfile(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @ManyToOne
    @JoinColumn(name = "university_id")
    var university: University? = null,
)
