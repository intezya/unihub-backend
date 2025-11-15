package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ClubDto
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.service.ClubService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student/clubs")
class ClubController(
    private val clubService: ClubService,
    private val studentProfileRepository: StudentProfileRepository,
) : ClubApi {

    @GetMapping
    override fun getClubs(@RequestParam(required = false) universityId: UUID?): List<ClubDto> {
        val actualUniversityId = universityId
            ?: studentProfileRepository.findAll().firstOrNull()?.university?.id
            ?: return emptyList()
        return clubService.getClubsForUniversity(actualUniversityId)
    }

    @GetMapping("/{id}")
    override fun getClubById(@PathVariable id: Long, @RequestParam(required = false) universityId: UUID?): ClubDto? {
        val actualUniversityId = universityId
            ?: studentProfileRepository.findAll().firstOrNull()?.university?.id
            ?: return null
        val clubs = clubService.getClubsForUniversity(actualUniversityId)
        return clubs.find { it.id == id }
    }
}
