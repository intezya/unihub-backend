package com.intezya.unihub.utils.error

/**
 * Pre-defined errors for all entities in the application
 */
object Errors {
    object User {
        fun notFound() = NotFoundException("User not found", "USER_NOT_FOUND")
        fun alreadyExists() = ConflictException("User already exists", "USER_ALREADY_EXISTS")
        fun unauthorized() = UnauthorizedException("User is not authorized", "USER_UNAUTHORIZED")
        fun forbidden() = ForbiddenException("User does not have permission", "USER_FORBIDDEN")
    }

    object University {
        fun notFound() = NotFoundException("University not found", "UNIVERSITY_NOT_FOUND")
    }

    object StudentProfile {
        fun notFound() = NotFoundException("Student not found", "STUDENT_NOT_FOUND")
    }

    object Schedule {
        fun notFound() = NotFoundException("Schedule not found", "SCHEDULE_NOT_FOUND")
    }

    object Lesson {
        fun notFound() = NotFoundException("Lesson not found", "LESSON_NOT_FOUND")
    }

    object News {
        fun notFound() = NotFoundException("News not found", "NEWS_NOT_FOUND")
    }

    object CertificateRequest {
        fun notFound() = NotFoundException("Certificate request not found", "CERTIFICATE_REQUEST_NOT_FOUND")
    }

    object Club {
        fun notFound() = NotFoundException("Club not found", "CLUB_NOT_FOUND")
    }

    object Project {
        fun notFound() = NotFoundException("Project not found", "PROJECT_NOT_FOUND")
    }

    object Internship {
        fun notFound() = NotFoundException("Internship not found", "INTERNSHIP_NOT_FOUND")
    }
}
