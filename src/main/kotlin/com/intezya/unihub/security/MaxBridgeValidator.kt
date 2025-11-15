package com.intezya.unihub.security

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

data class MaxUserData(
    val id: Long,
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val languageCode: String?,
    val photoUrl: String?,
)

@Component
class MaxBridgeValidator(
    @Value("\${max.bot.token}") private val botToken: String,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun validateInitData(initData: String): MaxUserData? {
        try {
            val params = parseInitData(initData)
            logger.warn("paramsing: $params")
            val receivedHash = params["hash"] ?: return null
            println("received hash: $receivedHash")

            val dataCheckString = params
                .filter { it.key != "hash" }
                .toSortedMap()
                .map { "${it.key}=${it.value}" }
                .joinToString("\n")
            println("data_check_string: $dataCheckString")

            val secretKey = hmacSha256("WebAppData".toByteArray(Charsets.UTF_8), botToken.toByteArray(Charsets.UTF_8))
            println("secret key: ${secretKey.joinToString("") { "%02x".format(it) }}")

            val calculatedHash = hmacSha256Hex(dataCheckString.toByteArray(Charsets.UTF_8), secretKey)
            println("calculated hash: $calculatedHash")
            println("hashes equal: ${calculatedHash == receivedHash}")

            if (calculatedHash != receivedHash) return null

            val userJson = params["user"] ?: return null
            println("user is $userJson")
            return parseUserData(userJson)
        } catch (e: Exception) {
            e.printStackTrace()
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

    private fun hmacSha256(data: ByteArray, key: ByteArray): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key, "HmacSHA256")
        mac.init(secretKey)
        return mac.doFinal(data)
    }

    private fun hmacSha256Hex(data: ByteArray, key: ByteArray): String {
        val result = hmacSha256(data, key)
        return result.joinToString("") { "%02x".format(it) }
    }
}
