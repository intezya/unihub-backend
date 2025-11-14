package com.intezya.unihub.service

import com.intezya.unihub.domain.entity.Internship
import com.intezya.unihub.domain.entity.InternshipStatus
import com.intezya.unihub.domain.repository.InternshipRepository
import com.intezya.unihub.domain.repository.UserRepository
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

data class ParsedInternship(
    val company: String,
    val status: String,
    val url: String?,
)

@Service
class InternshipParserService(
    private val internshipRepository: InternshipRepository,
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(InternshipParserService::class.java)
    private val POSTYPASHKI_URL = "https://postypashki.ru/стажировки/"

    // Запускается каждые 12 часов (43200000 миллисекунд)
    @Scheduled(fixedRate = 43200000, initialDelay = 5000)
    fun parseAndUpdateInternships() {
        logger.info("Starting internships parsing from postypashki.ru...")

        try {
            val parsedInternships = parsePostypashkiPage()
            logger.info("Parsed ${parsedInternships.size} internships from postypashki.ru")

            // Получаем первого пользователя для creator_id (опционально)
            val systemUser = userRepository.findAll().firstOrNull()

            parsedInternships.forEach { parsed ->
                updateOrCreateInternship(parsed, systemUser)
            }

            logger.info("Successfully updated internships database")
        } catch (e: Exception) {
            logger.error("Error parsing internships: ${e.message}", e)
        }
    }

    private fun parsePostypashkiPage(): List<ParsedInternship> {
        val result = mutableListOf<ParsedInternship>()

        try {
            val doc = Jsoup.connect(POSTYPASHKI_URL)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get()

            // Находим таблицу со стажировками
            val table = doc.select("table").firstOrNull() ?: return emptyList()
            val rows = table.select("tbody tr")

            // Пропускаем первую строку (заголовки)
            rows.drop(1).forEach { row ->
                val cells = row.select("td")
                if (cells.size >= 2) {
                    val companyCell = cells[0]
                    val statusCell = cells[1]

                    val link = companyCell.select("a").firstOrNull()
                    val company = link?.text()?.trim() ?: companyCell.text().trim()
                    val status = statusCell.text().trim()
                    val url = link?.attr("href")

                    if (company.isNotBlank()) {
                        result.add(
                            ParsedInternship(
                                company = company,
                                status = status,
                                url = url?.takeIf { it.isNotBlank() },
                            ),
                        )
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error parsing postypashki page: ${e.message}", e)
        }

        return result
    }

    private fun updateOrCreateInternship(parsed: ParsedInternship, creator: com.intezya.unihub.domain.entity.User?) {
        // Ищем существующую стажировку по названию компании
        val existingInternships = internshipRepository.findAll()
        val existing = existingInternships.find { it.companyName == parsed.company }

        if (existing != null) {
            // Обновляем статус
            existing.status = parseStatus(parsed.status)
            existing.externalUrl = parsed.url
            internshipRepository.save(existing)
            logger.debug("Updated internship: ${parsed.company}")
        } else {
            // Создаем новую стажировку
            val newInternship = Internship(
                companyName = parsed.company,
                title = "Стажировка в ${parsed.company}",
                description = "Стажировка в компании ${parsed.company}. Подробности по ссылке.",
                status = parseStatus(parsed.status),
                externalUrl = parsed.url,
                direction = detectDirection(parsed.company),
                isPaid = true,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusMonths(6),
                creator = creator, // Может быть null для автоматически спарсенных стажировок
                university = null, // Стажировки доступны всем университетам
            )

            internshipRepository.save(newInternship)
            logger.debug("Created new internship: ${parsed.company}")
        }
    }

    private fun parseStatus(statusText: String): InternshipStatus = when {
        statusText.contains("открыт", ignoreCase = true) -> InternshipStatus.ACTIVE
        statusText.contains("закрыт", ignoreCase = true) -> InternshipStatus.CLOSED
        statusText.contains("завершен", ignoreCase = true) -> InternshipStatus.COMPLETED
        else -> InternshipStatus.ACTIVE
    }

    private fun detectDirection(company: String): String = when {
        company.contains("Яндекс", ignoreCase = true) -> "IT"
        company.contains("Тинькофф", ignoreCase = true) -> "IT, Финансы"
        company.contains("ВК", ignoreCase = true) || company.contains("VK", ignoreCase = true) -> "IT"
        company.contains("Касперский", ignoreCase = true) -> "IT, Кибербезопасность"
        company.contains("Сбер", ignoreCase = true) -> "IT, Финансы"
        company.contains("Озон", ignoreCase = true) || company.contains(
            "Ozon",
            ignoreCase = true,
        ) -> "IT, E-commerce"

        else -> "Различные направления"
    }

    // Метод для ручного запуска парсинга (можно вызвать через эндпоинт)
    fun manualParse() {
        parseAndUpdateInternships()
    }
}
