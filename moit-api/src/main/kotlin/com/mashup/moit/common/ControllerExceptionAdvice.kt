package com.mashup.moit.common

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerExceptionAdvice {
    private val log: Logger = LoggerFactory.getLogger(ControllerExceptionAdvice::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<MoitApiResponse<Any>> {
        log.error("Exception handler", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                MoitApiResponse(
                    success = false,
                    data = null,
                    error = MoitApiErrorResponse(
                        code = MoitExceptionType.SYSTEM_FAIL.name,
                        message = exception.message,
                    ),
                )
            )
    }

    @ExceptionHandler(MoitException::class)
    fun handleMoitException(exception: MoitException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("MoitException handler", exception)
        return ResponseEntity.status(exception.httpStatusCode)
            .body(
                MoitApiResponse(
                    success = false,
                    data = null,
                    error = exception.toApiErrorResponse(),
                )
            )
    }

    private fun MoitException.toApiErrorResponse() = MoitApiErrorResponse(
        code = errorCode,
        message = message,
    )
}
