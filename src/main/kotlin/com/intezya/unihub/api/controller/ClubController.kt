package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.ClubDto
import com.intezya.unihub.api.dto.CreateClubDto
import com.intezya.unihub.domain.repository.ClubRepository
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.service.ClubService
import com.intezya.unihub.service.FileUploadService
import com.intezya.unihub.utils.error.NotFoundException
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/student/clubs")
class ClubController(
    private val clubService: ClubService,
    private val studentProfileRepository: StudentProfileRepository,
    private val clubRepository: ClubRepository,
    private val fileUploadService: FileUploadService,
) : ClubApi {

    @GetMapping
    override fun getClubs(@RequestParam(required = false) universityId: UUID?): List<ClubDto> = clubRepository.findAll()
        .map { ClubDto.from(it, logoUrl = it.imageObjectKey?.let { fileUploadService.getPublicUrl(it) }) }

    @GetMapping("/{id}")
    override fun getClubById(@PathVariable id: UUID, @RequestParam(required = false) universityId: UUID?): ClubDto? {
        val club = clubRepository.findById(id).orElseThrow {
            NotFoundException("Club with id $id not found")
        }

        val logoUrl = club.imageObjectKey?.let { fileUploadService.getPublicUrl(it) }

        return ClubDto.from(club, logoUrl = logoUrl)
    }

    @PostMapping
    fun createClub(@RequestBody clubDto: CreateClubDto): ClubDto {
        val club = clubService.create(clubDto)
        return ClubDto.from(club, logoUrl = null)
    }
}
