package com.intezya.unihub.utils.error

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val error: String,
    val message: String,
    val errorCode: String? = null,
    val path: String? = null,
    val details: Map<String, Any>? = null,
)
