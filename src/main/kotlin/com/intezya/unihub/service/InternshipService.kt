package com.intezya.unihub.service

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
                InternshipDto(
                    id = internship.id!!,
                    title = internship.title,
                    description = internship.description,
                    companyName = internship.companyName,
                    location = internship.location,
                    startDate = internship.startDate?.toString(),
                    endDate = internship.endDate?.toString(),
                    isPaid = internship.isPaid,
                )
            }
}

data class InternshipDto(
    val id: UUID,
    val title: String,
    val description: String,
    val companyName: String,
    val location: String?,
    val startDate: String?,
    val endDate: String?,
    val isPaid: Boolean?,
)
