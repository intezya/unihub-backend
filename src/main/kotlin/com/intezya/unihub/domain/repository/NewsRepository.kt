package com.intezya.unihub.domain.repository

import com.intezya.unihub.domain.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NewsRepository : JpaRepository<News, UUID> {
    fun findByUniversityIdAndIsPublishedTrueOrderByPublishedAtDesc(universityId: UUID): List<News>
}
