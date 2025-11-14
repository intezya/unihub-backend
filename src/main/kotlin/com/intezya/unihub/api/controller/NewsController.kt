package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.NewsDto
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.NewsService
import com.intezya.unihub.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/student/news")
@RequireUserType(UserType.STUDENT)
class NewsController(
    private val newsService: NewsService,
    private val studentService: StudentService,
) : NewsApi {

    @GetMapping
    override fun getNews(): List<NewsDto> {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        return newsService.getRecentNewsForUniversity(studentProfile.universityId, 20)
    }

    @GetMapping("/{id}")
    override fun getNewsById(@PathVariable id: Long): NewsDto? {
        val studentId = studentService.getCurrentStudentId()
        val studentProfile = studentService.getStudentProfile(studentId)
        val newsList = newsService.getRecentNewsForUniversity(studentProfile.universityId, 100)
        return newsList.find { it.id == id }
    }
}
