package com.intezya.unihub.utils.error

import org.springframework.http.HttpStatus

/**
 * Base exception for all application-specific exceptions
 */
open class AppException(
    message: String,
    val status: HttpStatus,
    val errorCode: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

/**
 * Thrown when a requested entity is not found
 */
class NotFoundException(
    message: String,
    errorCode: String = "NOT_FOUND",
) : AppException(
    message = message,
    status = HttpStatus.NOT_FOUND,
    errorCode = errorCode,
)

/**
 * Thrown when there's a conflict with the current state
 */
class ConflictException(
    message: String,
    errorCode: String = "CONFLICT",
) : AppException(
    message = message,
    status = HttpStatus.CONFLICT,
    errorCode = errorCode,
)

/**
 * Thrown when request validation fails
 */
class ValidationException(
    message: String,
    errorCode: String = "VALIDATION_ERROR",
) : AppException(
    message = message,
    status = HttpStatus.BAD_REQUEST,
    errorCode = errorCode,
)

/**
 * Thrown when business logic validation fails
 */
class BusinessException(
    message: String,
    errorCode: String = "BUSINESS_ERROR",
) : AppException(
    message = message,
    status = HttpStatus.UNPROCESSABLE_ENTITY,
    errorCode = errorCode,
)

/**
 * Thrown when there are not enough resources available
 */
class InsufficientResourceException(
    message: String,
    errorCode: String = "INSUFFICIENT_RESOURCE",
) : AppException(
    message = message,
    status = HttpStatus.CONFLICT,
    errorCode = errorCode,
)

/**
 * Thrown when access to a resource is forbidden
 */
class ForbiddenException(
    message: String = "Access forbidden",
    errorCode: String = "FORBIDDEN",
) : AppException(
    message = message,
    status = HttpStatus.FORBIDDEN,
    errorCode = errorCode,
)

/**
 * Thrown when authentication fails
 */
class UnauthorizedException(
    message: String = "Unauthorized",
    errorCode: String = "UNAUTHORIZED",
) : AppException(
    message = message,
    status = HttpStatus.UNAUTHORIZED,
    errorCode = errorCode,
)
