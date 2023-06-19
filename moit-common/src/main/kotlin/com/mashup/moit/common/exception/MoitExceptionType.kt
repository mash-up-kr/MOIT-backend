package com.mashup.moit.common.exception

enum class MoitExceptionType(
    val message: String,
    val errorCode: String,
    val httpStatusCode: Int,
) {
    // USER
    AUTH_ERROR("유저 프로세스에서 오류가 발생했습니다.", "U000_AUTH_ERROR", 500),
    EMPTY_AUTHORIZATION_HEADER("Not Exist Authorization Header", "U001_EMPTY_AUTHORIZATION_HEADER", 400),
    INVALID_USER_AUTH_TOKEN("Invalid JWT Token", "U002_INVALID_TOKEN", 400),
    INVALID_AUTH_PROVIDER("Invalid provider by auth0. Check social section of auth0", "U003_INVALID_AUTH_PROVIDER", 500),
    // COMMON
    NOT_EXIST("존재하지 않습니다.", "C001_NOT_EXIST", 404),
    SYSTEM_FAIL("Internal Server Error.", "C002_SYSTEM_FAIL", 500),
    INVALID_ACCESS("Invalid Access", "C003_INVALID_ACCESS", 403),
    ALREADY_EXIST("Already Exist", "C004_ALREADY_EXIST", 409),

    // ATTENDANCE
    ATTENDANCE_NOT_STARTED("스터디 출석이 아직 시작되지 않았습니다.", "A001_NOT_STARTED", 400),
    INVALID_ATTENDANCE_KEYWORD("Invalid Attendance Keyword", "A001_INVALID_ATTENDANCE_KEYWORD", 400),
    ;
}
