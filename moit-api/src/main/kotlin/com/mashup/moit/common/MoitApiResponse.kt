package com.mashup.moit.common

import com.mashup.moit.common.exception.MoitExceptionType

data class MoitApiResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val error: MoitApiErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): MoitApiResponse<T> {
            return MoitApiResponse(
                success = true,
                data = data,
            )
        }

        fun fail(moitExceptionType: MoitExceptionType, message: String?): MoitApiResponse<Unit> {
            return MoitApiResponse(
                success = true,
                data = null,
                error = MoitApiErrorResponse(
                    code = moitExceptionType.errorCode,
                    message = message ?: moitExceptionType.message,
                )
            )
        }

    }
}

data class MoitApiErrorResponse(
    val code: String,
    val message: String?,
)
