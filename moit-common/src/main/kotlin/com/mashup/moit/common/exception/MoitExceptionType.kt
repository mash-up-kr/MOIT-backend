package com.mashup.moit.common.exception

enum class MoitExceptionType (
    val message: String,
) {
    // COMMON
    NOT_EXIST("존재하지 않습니다."),
    SYSTEM_FAIL("Internal Server Error."),
    ;
}
