package com.intezya.unihub.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
data class University(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "address", nullable = false)
    val address: String,
)
