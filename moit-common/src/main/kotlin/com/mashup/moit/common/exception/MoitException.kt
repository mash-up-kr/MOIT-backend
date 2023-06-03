package com.mashup.moit.common.exception

class MoitException(
    val errorCode: String,
    val httpStatusCode: Int,
    override val message: String,
) : RuntimeException() {
    companion object {
        fun of(type: MoitExceptionType): MoitException {
            return MoitException(
                errorCode = type.errorCode,
                httpStatusCode = type.httpStatusCode,
                message = type.message,
            )
        }

        fun of(type: MoitExceptionType, message: String): MoitException {
            return MoitException(
                errorCode = type.errorCode,
                httpStatusCode = type.httpStatusCode,
                message = message,
            )
        }
    }
}
