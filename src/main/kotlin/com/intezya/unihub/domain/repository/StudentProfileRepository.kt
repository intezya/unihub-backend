package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.StudentProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentProfileRepository : JpaRepository<StudentProfile, UUID>
