package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.News
import java.time.format.DateTimeFormatter

data class NewsDto(
    val id: Long,
    val title: String,
    val text: String,
    val image: String?,
    val createdAt: String,
    val author: String,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        fun from(news: News, imageUrl: String?, authorName: String = "Администрация"): NewsDto = NewsDto(
            id = news.id.hashCode().toLong(),
            title = news.title,
            text = news.content,
            image = imageUrl,
            createdAt = (news.publishedAt ?: news.createdAt).atZone(java.time.ZoneId.systemDefault())
                .format(formatter),
            author = authorName,
        )
    }
}
