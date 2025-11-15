package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.InternshipDto
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.service.InternshipService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/student/internships")
class InternshipController(
    private val internshipService: InternshipService,
    private val studentProfileRepository: StudentProfileRepository,
) : InternshipApi {

    @GetMapping
    override fun getInternships(@RequestParam(required = false) universityId: UUID?): List<InternshipDto> {
        val actualUniversityId = universityId
            ?: studentProfileRepository.findAll().firstOrNull()?.university?.id
            ?: return emptyList()
        return internshipService.getInternshipsForUniversity(actualUniversityId)
    }
}
