package com.intezya.unihub.service

import com.intezya.unihub.api.dto.ClubDto
import com.intezya.unihub.api.dto.CreateClubDto
import com.intezya.unihub.domain.entity.Club
import com.intezya.unihub.domain.entity.ClubCategory
import com.intezya.unihub.domain.repository.ClubRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val avatarUrlService: AvatarUrlService,
) {
    fun create(clubDto: CreateClubDto): Club {
        val club = Club(
            name = clubDto.name,
            description = clubDto.description,
            category = ClubCategory.valueOf(clubDto.category.uppercase()),
        )
        return clubRepository.save(club)
    }

    fun getClubsForUniversity(universityId: UUID): List<ClubDto> =
        clubRepository.findByUniversityIdOrderByNameAsc(universityId)
            .map { club ->
                ClubDto.from(
                    club = club,
                    logoUrl = club.imageObjectKey?.let { avatarUrlService.generatePresignedUrl(it) },
                    members = club.members.size,
                    category = club.category.displayName,
                )
            }
}
