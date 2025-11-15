package com.intezya.unihub.api.controller

import com.intezya.unihub.domain.entity.StudentProfile
import com.intezya.unihub.domain.entity.User
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.domain.repository.StudentProfileRepository
import com.intezya.unihub.domain.repository.UserRepository
import com.intezya.unihub.security.JwtService
import com.intezya.unihub.security.MaxBridgeValidator
import com.intezya.unihub.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class MaxAuthRequest(
    val initData: String,
)

data class MaxAuthResponse(
    val token: String,
    val userId: String,
    val maxUserId: Long,
)

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val maxBridgeValidator: MaxBridgeValidator,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val studentProfileRepository: StudentProfileRepository,
) : AuthApi {
    val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/max")
    override fun authenticateWithMax(@RequestBody request: MaxAuthRequest): ResponseEntity<MaxAuthResponse> {
        // Валидируем initData
        // Находим или создаем пользователя
        println(request.initData)

        val maxUserData = maxBridgeValidator.validateInitData(request.initData)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val user: User = try {
            val newUser = userRepository.save(
                User(
                    serviceId = UUID.randomUUID(),
                    userType = UserType.STUDENT,
                    avatarUrl = maxUserData.photoUrl,
                    maxUserId = maxUserData.id,
                ),
            )
            val profile = StudentProfile(
                user = newUser,
                firstName = maxUserData.firstName ?: "",
                lastName = maxUserData.lastName ?: "",
                studentNumber = maxUserData.id.toString(),
                groupName = "",
                direction = "",
            )
            studentProfileRepository.save(profile)
            newUser
        } catch (e: Exception) {
            userRepository.findByMaxUserId(maxUserData.id).orElseThrow()
        }

        // Генерируем JWT токен
        val token = jwtService.generateToken(user.id, maxUserData.id)

        return ResponseEntity.ok(
            MaxAuthResponse(
                token = token,
                userId = user.id.toString(),
                maxUserId = maxUserData.id,
            ),
        )
    }
}
