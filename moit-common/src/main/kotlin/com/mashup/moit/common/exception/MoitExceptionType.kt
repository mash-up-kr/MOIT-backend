package com.mashup.moit.common.exception

enum class MoitExceptionType(
    val message: String,
    val errorCode: String,
    val httpStatusCode: Int,
) {
    // USER
    INVALID_USER_AUTH_TOKEN("Invalid JWT Token", "U001_INVALID_TOKEN", 400),
    INVALID_AUTH_PROVIDER("Invalid provider by auth0. Check social section of auth0", "U002_INVALID_AUTH_PROVIDER", 500),
    
    // COMMON
    NOT_EXIST("존재하지 않습니다.", "C001_NOT_EXIST", 404),
    SYSTEM_FAIL("Internal Server Error.", "C002_SYSTEM_FAIL", 500),
    ;
}
