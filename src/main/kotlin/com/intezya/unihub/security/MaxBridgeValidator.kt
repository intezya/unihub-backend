package com.intezya.unihub.security

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class MaxUserData(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val languageCode: String?,
    val photoUrl: String?,
)

@Component
class MaxBridgeValidator {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun validateInitData(initData: String): MaxUserData? {
        try {
            val params = parseInitData(initData)
            logger.warn("paramsing: $params")

            // Просто достаем данные юзера без валидации хеша
            val userJson = params["user"] ?: return null
            logger.warn("user is $userJson")
            return parseUserData(userJson)
        } catch (e: Exception) {
            logger.error("Error parsing init data", e)
            return null
        }
    }

    private fun parseInitData(initData: String): Map<String, String> = initData.split("&").associate {
        val parts = it.split("=", limit = 2)
        val key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8)
        val value = if (parts.size > 1) URLDecoder.decode(parts[1], StandardCharsets.UTF_8) else ""
        key to value
    }

    private fun parseUserData(userJson: String): MaxUserData {
        val userMap = userJson
            .removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .associate {
                val parts = it.split(":", limit = 2)
                val key = parts[0].trim().removeSurrounding("\"")
                val value = if (parts.size > 1) parts[1].trim().removeSurrounding("\"") else ""
                key to value
            }

        return MaxUserData(
            id = userMap["id"]?.toLongOrNull() ?: 0,
            firstName = userMap["first_name"],
            lastName = userMap["last_name"],
            username = userMap["username"],
            languageCode = userMap["language_code"],
            photoUrl = userMap["photo_url"],
        )
    }
}
