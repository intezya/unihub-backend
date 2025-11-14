package com.intezya.unihub.service

import com.intezya.unihub.api.dto.InternshipDto
import com.intezya.unihub.domain.repository.InternshipRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InternshipService(
    private val internshipRepository: InternshipRepository,
) {

    fun getInternshipsForUniversity(universityId: UUID): List<InternshipDto> =
        internshipRepository.findByUniversityIdOrderByTitleAsc(universityId)
            .map { internship ->
                InternshipDto.from(internship)
            }
}
