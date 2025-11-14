package com.intezya.unihub.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

/**
 * Компонент для применения критических исправлений схемы БД при старте приложения.
 * Используется когда liquibase выключен, но нужно применить конкретные изменения.
 */
@Component
class DatabaseSchemaFixer(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val logger = LoggerFactory.getLogger(DatabaseSchemaFixer::class.java)

    @PostConstruct
    fun fixInternshipsConstraints() {
        try {
            logger.info("Applying database schema fixes...")

            // Делаем creator_id nullable
            jdbcTemplate.execute(
                """
                DO ${'$'}${'$'} 
                BEGIN
                    IF EXISTS (
                        SELECT 1 FROM information_schema.columns 
                        WHERE TABLE_NAME = 'internships' 
                        AND COLUMN_NAME = 'creator_id' 
                        AND is_nullable = 'NO'
                    ) THEN
                        ALTER TABLE internships ALTER COLUMN creator_id DROP NOT NULL;
                        RAISE NOTICE 'Fixed creator_id constraint';
                    END IF;
                END ${'$'}${'$'};
                """.trimIndent(),
            )

            // Делаем university_id nullable
            jdbcTemplate.execute(
                """
                DO ${'$'}${'$'} 
                BEGIN
                    IF EXISTS (
                        SELECT 1 FROM information_schema.columns 
                        WHERE TABLE_NAME = 'internships' 
                        AND COLUMN_NAME = 'university_id' 
                        AND is_nullable = 'NO'
                    ) THEN
                        ALTER TABLE internships ALTER COLUMN university_id DROP NOT NULL;
                        RAISE NOTICE 'Fixed university_id constraint';
                    END IF;
                END ${'$'}${'$'};
                """.trimIndent(),
            )

            logger.info("Database schema fixes applied successfully")
        } catch (e: Exception) {
            logger.error("Failed to apply database schema fixes", e)
            // Не бросаем исключение, чтобы приложение могло стартовать
        }
    }
}
