package com.intezya.unihub.service

import com.intezya.unihub.api.dto.InternshipDto
import com.intezya.unihub.domain.repository.InternshipRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InternshipService(
    private val internshipRepository: InternshipRepository,
    private val avatarUrlService: AvatarUrlService,
) {

    fun getInternshipsForUniversity(universityId: UUID): List<InternshipDto> =
        internshipRepository.findByUniversityIdOrderByTitleAsc(universityId)
            .map { internship ->
                val logoUrl = internship.logoUrl?.let { avatarUrlService.generatePresignedUrl(it) }
                InternshipDto.from(internship, logoUrl)
            }
}
