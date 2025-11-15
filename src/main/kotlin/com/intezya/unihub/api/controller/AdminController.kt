package com.intezya.unihub.api.controller

import com.intezya.unihub.api.dto.AdminUserDto
import com.intezya.unihub.api.dto.ChangeRoleAdminRequest
import com.intezya.unihub.domain.entity.CertificateRequestStatus
import com.intezya.unihub.domain.entity.UserType
import com.intezya.unihub.security.RequireUserType
import com.intezya.unihub.service.AdminService
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api/admin")
@RequireUserType(UserType.UNIVERSITY_ADMIN, UserType.ADMIN)
@RestController
class AdminController(
    private val adminService: AdminService,
) {

    @PostMapping("/migrations/run")
    fun runMigrations(): Map<String, String> = mapOf("result" to adminService.runMigrations())

    @GetMapping("/system/stats")
    fun systemStats() = adminService.systemStats()

    @PostMapping("/reindex")
    fun reindex(): Map<String, String> = mapOf("result" to adminService.reindex())

    @PostMapping("/users/{id}/role")
    fun changeUserRole(@PathVariable id: UUID, @RequestBody request: ChangeRoleAdminRequest): AdminUserDto =
        adminService.changeRole(id, request.newRole)

    @PostMapping("/users/{id}/activate")
    fun activateUser(@PathVariable id: UUID): AdminUserDto = adminService.setActive(id, true)

    @PostMapping("/users/{id}/deactivate")
    fun deactivateUser(@PathVariable id: UUID): AdminUserDto = adminService.setActive(id, false)

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: UUID): AdminUserDto = adminService.getUserById(id)

    @GetMapping("/users")
    fun listUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) role: UserType?,
        @RequestParam(required = false) active: Boolean?,
    ) = adminService.listUsers(page, size, role, active)
}

data class UpdateCertificateRequestRequest(
    val status: CertificateRequestStatus?,
    val fileObjectKey: String?,
)
