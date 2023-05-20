package com.mashup.moit.common.exception

import java.lang.RuntimeException

class MoitException(
    val code: String,
    override val message: String,
) : RuntimeException() {
    companion object {
        fun of(type: MoitExceptionType): MoitException {
            return MoitException(
                code = type.name,
                message = type.message,
            )
        }

        fun of(type: MoitExceptionType, message: String): MoitException {
            return MoitException(
                code = type.name,
                message = message,
            )
        }
    }
}
