package com.intezya.unihub.service

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
                ClubDto(
                    id = club.id,
                    name = club.name,
                    description = club.description,
                    imageUrl = club.imageObjectKey?.let { avatarUrlService.generatePresignedUrl(it) },
                )
            }
}

data class ClubDto(
    val id: UUID,
    val name: String,
    val description: String,
    val imageUrl: String?,
)
