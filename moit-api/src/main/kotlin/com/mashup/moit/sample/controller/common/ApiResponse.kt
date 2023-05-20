package com.mashup.moit.sample.controller.common

data class ApiResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val error: ApiErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
            )
        }
    }
}

data class ApiErrorResponse(
    val code: String,
    val message: String?,
)
