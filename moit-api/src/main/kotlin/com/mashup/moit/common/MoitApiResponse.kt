package com.mashup.moit.common

data class MoitApiResponse<T>(
    val success: Boolean = true,
    val data: T? = null,
    val error: MoitApiErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T): MoitApiResponse<T> {
            return MoitApiResponse(
                success = true,
                data = data
            )
        }
    }
}

data class MoitApiErrorResponse(
    val code: String,
    val message: String?,
)
