package com.mashup.moit.common

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@RestControllerAdvice
class ControllerExceptionAdvice {
    private val log: Logger = LoggerFactory.getLogger(ControllerExceptionAdvice::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<MoitApiResponse<Any>> {
        log.error("Exception handler", ex)
        return errorResponse(MoitExceptionType.SYSTEM_FAIL, ex.message)
    }

    @ExceptionHandler(MoitException::class)
    fun handleMoitException(ex: MoitException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("MoitException handler", ex)
        return errorResponse(ex.httpStatusCode, ex.toApiErrorResponse())
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    protected fun handleMissingServletRequestParameterException(ex: MissingServletRequestParameterException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("MissingServletRequestParameterException handler", ex)
        return errorResponse(MoitExceptionType.INVALID_INPUT, ex.message)
    }

    @ExceptionHandler(BindException::class)
    protected fun handleBindException(ex: BindException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("BindException handler", ex)
        return errorResponse(MoitExceptionType.INVALID_INPUT, ex.message)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("MethodArgumentTypeMismatchException handler", ex)
        return errorResponse(MoitExceptionType.METHOD_ARGUMENT_TYPE_MISMATCH_VALUE, ex.message)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("HttpRequestMethodNotSupportedException handler", ex)
        return errorResponse(MoitExceptionType.HTTP_REQUEST_METHOD_NOT_SUPPORTED, ex.message)
    }

    @ExceptionHandler(AccessDeniedException::class)
    protected fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("AccessDeniedException handler", ex)
        return errorResponse(MoitExceptionType.ACCESS_DENIED, ex.message)
    }

    @ExceptionHandler(AuthenticationException::class)
    protected fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("AuthenticationException handler", ex)
        return errorResponse(MoitExceptionType.AUTHENTICATION_FAILURE, ex.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<MoitApiResponse<Any>> {
        log.error("MethodArgumentNotValidException handler", e)
        val errorMessage = e.allErrors.joinToString(" ,")
        return errorResponse(MoitExceptionType.ARGUMENT_NOT_VALID, errorMessage)
    }

    private fun MoitException.toApiErrorResponse() = MoitApiErrorResponse(
        code = errorCode,
        message = message,
    )

    private fun errorResponse(exceptionType: MoitExceptionType, message: String?) = errorResponse(exceptionType.httpStatusCode, MoitApiErrorResponse(exceptionType.name, message))
    private fun errorResponse(status: Int, errorResponse: MoitApiErrorResponse) = errorResponse(HttpStatus.valueOf(status), errorResponse)

    private fun errorResponse(status: HttpStatus, errorResponse: MoitApiErrorResponse): ResponseEntity<MoitApiResponse<Any>> = ResponseEntity.status(status)
        .body(
            MoitApiResponse(
                success = false,
                data = null,
                error = errorResponse,
            )
        )

}
