package com.intezya.unihub.service

import com.intezya.unihub.api.dto.ClubDto
import com.intezya.unihub.domain.repository.ClubRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val avatarUrlService: AvatarUrlService,
) {

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
