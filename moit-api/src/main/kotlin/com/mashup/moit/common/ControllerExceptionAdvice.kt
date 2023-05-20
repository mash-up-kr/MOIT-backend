package com.mashup.moit.common

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionAdvice {
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ApiResponse(
                    success = false,
                    data = null,
                    error = ApiErrorResponse(
                        code = MoitExceptionType.SYSTEM_FAIL.name,
                        message = exception.message,
                    ),
                )
            )
    }

    @ExceptionHandler(MoitException::class)
    fun handleDoriDoriException(exception: MoitException): ResponseEntity<ApiResponse<Any>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse(
                    success = false,
                    data = null,
                    error = exception.toApiErrorResponse(),
                )
            )
    }

    private fun MoitException.toApiErrorResponse() = ApiErrorResponse(
        code = code,
        message = message,
    )
}
