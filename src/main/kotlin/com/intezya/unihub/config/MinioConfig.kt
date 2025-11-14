package com.intezya.unihub.config

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.SetBucketPolicyArgs
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig(
    @Value("\${minio.endpoint}") private val endpoint: String,
    @Value("\${minio.public-endpoint}") private val publicEndpoint: String,
    @Value("\${minio.access-key}") private val accessKey: String,
    @Value("\${minio.secret-key}") private val secretKey: String,
    @Value("\${minio.bucket}") private val bucket: String,
) {

    private val logger = LoggerFactory.getLogger(MinioConfig::class.java)

    @Bean
    fun minioClient(): MinioClient = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build()

    @Bean
    fun publicMinioClient(): MinioClient = MinioClient.builder()
        .endpoint(publicEndpoint)
        .credentials(accessKey, secretKey)
        .build()

    @Bean
    fun minioPublicEndpoint(): String = publicEndpoint

    @PostConstruct
    fun initializeBucket() {
        val client = minioClient()
        try {
            val bucketExists = client.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build(),
            )

            if (!bucketExists) {
                logger.info("Bucket '$bucket' does not exist. Creating...")
                client.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build(),
                )
                logger.info("Bucket '$bucket' created successfully")

                // Устанавливаем публичную политику для чтения объектов
                val policy = """
                    {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Effect": "Allow",
                                "Principal": {"AWS": "*"},
                                "Action": ["s3:GetObject"],
                                "Resource": ["arn:aws:s3:::$bucket/*"]
                            }
                        ]
                    }
                """.trimIndent()

                client.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                        .bucket(bucket)
                        .config(policy)
                        .build(),
                )
                logger.info("Bucket policy set successfully")
            } else {
                logger.info("Bucket '$bucket' already exists")
            }
        } catch (e: Exception) {
            logger.error("Error initializing MinIO bucket: ${e.message}", e)
            throw RuntimeException("Failed to initialize MinIO bucket", e)
        }
    }
}
