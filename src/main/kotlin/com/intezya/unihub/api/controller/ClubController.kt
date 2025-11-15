package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ClubDto
import com.intezya.unihub.api.dto.CreateClubDto
import com.intezya.unihub.domain.repository.ClubRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.service.ClubService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student/clubs")
class ClubController(
    private val clubService: ClubService,
    private val studentProfileRepository: StudentProfileRepository,
    private val clubRepository: ClubRepository,
) : ClubApi {

    @GetMapping
    override fun getClubs(@RequestParam(required = false) universityId: UUID?): List<ClubDto> =
        clubRepository.findAll().map { ClubDto.from(it, logoUrl = null) }

    @GetMapping("/{id}")
    override fun getClubById(@PathVariable id: Long, @RequestParam(required = false) universityId: UUID?): ClubDto? {
        val actualUniversityId = universityId
            ?: studentProfileRepository.findAll().firstOrNull()?.university?.id
            ?: return null
        val clubs = clubService.getClubsForUniversity(actualUniversityId)
        return clubs.find { it.id == id }
    }

    @PostMapping
    fun createClub(@RequestBody clubDto: CreateClubDto): ClubDto {
        val club = clubService.create(clubDto)
        return ClubDto.from(club, logoUrl = null)
    }
}
