package com.intezya.unihub.api.controller

import com.intezya.unihub.security.JwtService
import com.intezya.unihub.security.MaxBridgeValidator
import com.intezya.unihub.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
) {

    @PostMapping("/max")
    fun authenticateWithMax(@RequestBody request: MaxAuthRequest): ResponseEntity<MaxAuthResponse> {
        // Валидируем initData
        val maxUserData = maxBridgeValidator.validateInitData(request.initData)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        // Находим или создаем пользователя
        val user = userService.findOrCreateByMaxId(
            maxUserId = maxUserData.id,
            firstName = maxUserData.firstName,
            lastName = maxUserData.lastName,
            photoUrl = maxUserData.photoUrl,
        )

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
