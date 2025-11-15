package com.intezya.unihub.service

import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FileUploadService(
    @Qualifier("minioClient") private val minioClient: MinioClient,
    @Value("\${minio.bucket}") private val bucket: String,
    private val avatarUrlService: AvatarUrlService,
) {

    private val logger = LoggerFactory.getLogger(FileUploadService::class.java)

    fun uploadAvatar(userId: String, file: MultipartFile): String {
        val objectName = "user-avatars/$userId.jpg"

        try {
            file.inputStream.use { inputStream ->
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucket)
                        .`object`(objectName)
                        .stream(inputStream, file.size, -1)
                        .contentType(file.contentType ?: "image/jpeg")
                        .build(),
                )
            }

            logger.info("Avatar uploaded successfully for user: $userId")

            // Возвращаем публичный URL
            return getPublicUrl(objectName)
        } catch (e: Exception) {
            logger.error("Failed to upload avatar for user: $userId", e)
            throw RuntimeException("Failed to upload avatar", e)
        }
    }

    fun uploadFile(folder: String, file: MultipartFile): String {
        val fileExtension = file.originalFilename?.substringAfterLast('.', "") ?: "bin"
        val objectName = "$folder/${UUID.randomUUID()}.$fileExtension"

        try {
            file.inputStream.use { inputStream ->
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucket)
                        .`object`(objectName)
                        .stream(inputStream, file.size, -1)
                        .contentType(file.contentType ?: "application/octet-stream")
                        .build(),
                )
            }

            logger.info("File uploaded successfully: $objectName")

            // Возвращаем публичный URL
            return getPublicUrl(objectName)
        } catch (e: Exception) {
            logger.error("Failed to upload file: ${file.originalFilename}", e)
            throw RuntimeException("Failed to upload file", e)
        }
    }

    fun getPublicUrl(objectName: String): String {
        // Для публичных bucket можно использовать прямую ссылку
        return avatarUrlService.generatePresignedUrl(objectName)
    }
}
