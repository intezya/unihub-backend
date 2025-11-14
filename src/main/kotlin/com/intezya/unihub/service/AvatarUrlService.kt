package com.intezya.unihub.service

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AvatarUrlService(
    @Qualifier("publicMinioClient") private val minioClient: MinioClient,
    @Value("\${minio.bucket}") private val bucket: String,
) {

    fun getAvatarUrl(userId: String): String {
        val objectName = "user-avatars/$userId.jpg"
        return generatePresignedUrl(objectName)
    }

    fun generatePresignedUrl(objectKey: String): String = minioClient.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .bucket(bucket)
            .`object`(objectKey)
            .expiry(1, TimeUnit.DAYS)
            .build(),
    )
}
