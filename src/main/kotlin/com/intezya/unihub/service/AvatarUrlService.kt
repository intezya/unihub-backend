package com.intezya.unihub.service

import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AvatarUrlService(
    private val minioClient: MinioClient,
    @Value("\${minio.bucket}") private val bucket: String,
) {

    fun getAvatarUrl(userId: String): String {
        val objectName = "user-avatars/$userId.jpg"

        return minioClient.getPresignedObjectUrl(Method.GET, bucket, objectName, 60 * 60 * 24, null)
    }
}
