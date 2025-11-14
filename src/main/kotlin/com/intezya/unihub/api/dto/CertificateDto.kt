package com.intezya.unihub.api.dto

import com.intezya.unihub.domain.entity.CertificateRequest
import com.intezya.unihub.domain.entity.CertificateRequestStatus
import java.time.format.DateTimeFormatter

data class CertificateDto(
    val id: Long,
    val type: String,
    val status: String,
    val requestDate: String,
    val issueDate: String?,
) {
    companion object {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        fun from(request: CertificateRequest): CertificateDto {
            val status = when (request.status) {
                CertificateRequestStatus.APPROVED -> "Готова"
                CertificateRequestStatus.NEW -> "В обработке"
                CertificateRequestStatus.REJECTED -> "Отклонена"
            }

            val type = when (request.type.name) {
                "STUDY_CERTIFICATE" -> "Справка об обучении"
                else -> "Справка"
            }

            return CertificateDto(
                id = request.id.hashCode().toLong(),
                type = type,
                status = status,
                requestDate = request.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE),
                issueDate = request.processedAt?.format(DateTimeFormatter.ISO_LOCAL_DATE),
            )
        }
    }
}
