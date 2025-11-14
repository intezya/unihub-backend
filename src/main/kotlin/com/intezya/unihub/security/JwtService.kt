package com.intezya.unihub.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtService(
    @field:Value("\${jwt.secret}") private val secret: String,
    @field:Value("\${jwt.expiration:86400000}") private val expiration: Long = 86400000, // 24 часа
) {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateToken(userId: UUID, maxUserId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(userId.toString())
            .claim("maxUserId", maxUserId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun getUserIdFromToken(token: String): UUID? = try {
        val claims = getClaims(token)
        UUID.fromString(claims.subject)
    } catch (e: Exception) {
        null
    }

    fun getMaxUserIdFromToken(token: String): Long? = try {
        val claims = getClaims(token)
        claims.get("maxUserId", java.lang.Long::class.java)?.toLong()
    } catch (e: Exception) {
        null
    }

    private fun getClaims(token: String): Claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .payload
}
