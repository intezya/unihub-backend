package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NewsDto
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.service.NewsService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student/news")
class NewsController(
    private val newsService: NewsService,
    private val studentProfileRepository: StudentProfileRepository,
) : NewsApi {

    @GetMapping
    override fun getNews(@RequestParam(required = false) universityId: UUID?): List<NewsDto> {
        val actualUniversityId = universityId
            ?: studentProfileRepository.findAll().firstOrNull()?.university?.id
            ?: return emptyList()
        return newsService.getRecentNewsForUniversity(actualUniversityId, 20)
    }

    @GetMapping("/{id}")
    override fun getNewsById(@PathVariable id: Long, @RequestParam(required = false) universityId: UUID?): NewsDto? {
        val actualUniversityId = universityId
            ?: studentProfileRepository.findAll().firstOrNull()?.university?.id
            ?: return null
        val newsList = newsService.getRecentNewsForUniversity(actualUniversityId, 100)
        return newsList.find { it.id == id }
    }
}
