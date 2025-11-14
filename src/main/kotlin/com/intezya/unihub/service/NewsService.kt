package com.intezya.unihub.service

import com.intezya.unihub.domain.repository.NewsRepository
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class NewsService(
    private val newsRepository: NewsRepository,
    private val avatarUrlService: AvatarUrlService,
) {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun getRecentNewsForUniversity(
        universityId: UUID,
        limit: Int = 5,
    ): List<com.intezya.unihub.api.controller.NewsDto> =
        newsRepository.findByUniversityIdAndIsPublishedTrueOrderByPublishedAtDesc(universityId)
            .take(limit)
            .map { news ->
                com.intezya.unihub.api.controller.NewsDto(
                    id = news.id!!,
                    title = news.title,
                    content = news.content,
                    imageUrl = news.imageObjectKey?.let { avatarUrlService.generatePresignedUrl(it) },
                    createdAt = news.createdAt.format(formatter),
                    publishedAt = news.publishedAt?.format(formatter),
                )
            }
}
