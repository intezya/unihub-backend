package com.intezya.unihub.service

import com.intezya.unihub.api.dto.NewsDto
import com.intezya.unihub.domain.repository.NewsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewsService(
    private val newsRepository: NewsRepository,
    private val avatarUrlService: AvatarUrlService,
) {
    fun getRecentNewsForUniversity(universityId: UUID, limit: Int = 5): List<NewsDto> =
        newsRepository.findByUniversityIdAndIsPublishedTrueOrderByPublishedAtDesc(universityId)
            .take(limit)
            .map { news ->
                NewsDto.from(
                    news = news,
                    imageUrl = news.imageObjectKey?.let { avatarUrlService.generatePresignedUrl(it) },
                    authorName = "Администрация",
                )
            }
}
