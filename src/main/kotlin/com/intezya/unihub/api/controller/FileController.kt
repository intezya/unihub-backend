package com.intezya.unihub.api.controller

import com.intezya.unihub.service.FileUploadService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
@Tag(name = "Files", description = "File upload and management")
class FileController(
    private val fileUploadService: FileUploadService,
) {

    @PostMapping("/avatar/{userId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Upload user avatar",
        description = "Uploads avatar for a specific user and returns public URL",
    )
    fun uploadAvatar(
        @PathVariable userId: String,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<Map<String, String>> {
        val url = fileUploadService.uploadAvatar(userId, file)
        return ResponseEntity.ok(mapOf("url" to url))
    }

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "Upload file", description = "Uploads file to specified folder and returns public URL")
    fun uploadFile(
        @RequestParam("folder") folder: String,
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<Map<String, String>> {
        val url = fileUploadService.uploadFile(folder, file)
        return ResponseEntity.ok(mapOf("url" to url))
    }
}
