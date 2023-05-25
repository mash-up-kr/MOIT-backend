package com.mashup.moit.common.exception

enum class MoitExceptionType(
    val message: String,
    val errorCode: String,
    val httpStatusCode: Int,
) {
    // COMMON
    NOT_EXIST("존재하지 않습니다.", "NOT_EXIST", 404),
    SYSTEM_FAIL("Internal Server Error.", "SYSTEM_FAIL", 500),
    ;
}
