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
    EMPTY_FCM_TOKEN("Not Exist FCM Token", "U004_EMPTY_FCM_TOKEN",400),
    
    // COMMON
    NOT_EXIST("존재하지 않습니다.", "C001_NOT_EXIST", 404),
    SYSTEM_FAIL("Internal Server Error.", "C002_SYSTEM_FAIL", 500),
    INVALID_ACCESS("Invalid Access", "C003_INVALID_ACCESS", 403),
    ALREADY_EXIST("Already Exist", "C004_ALREADY_EXIST", 409),
    INVALID_INPUT("Invalid Input", "C004_INVALID_INPUT", 400),
    METHOD_ARGUMENT_TYPE_MISMATCH_VALUE("Request method argument type mismatch", "C005_TYPE_MISMATCH_VALUE", 400),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("HTTP request method not supported", "C006_HTTP_METHOD_NOT_SUPPORTED", 400),
    ACCESS_DENIED("Access denied. Check authentication.", "C007_ACCESS_DENIED", 403),
    AUTHENTICATION_FAILURE("Authentication failed. Check login.", "C008_AUTHENTICATION_FAILURE", 401),
    ARGUMENT_NOT_VALID("Method Argument Not Valid. Check argument validation.", "C009_ARGUMENT_NOT_VALID", 400),

    // ATTENDANCE
    ATTENDANCE_NOT_STARTED("스터디 출석이 아직 시작되지 않았습니다.", "A001_NOT_STARTED", 400),
    INVALID_ATTENDANCE_KEYWORD("Invalid Attendance Keyword", "A001_INVALID_ATTENDANCE_KEYWORD", 400),

    // MOIT
    ONLY_MOIT_MASTER("해당 유저는 모잇 마스터 권한이 아닙니다.", "M001_ONLY_MOIT_MASTER", 400),

    // NOTIFICATION 
    INVALID_NOTIFICATION_TYPE("Invalid Notification Type", "N001_INVALID_NOTIFICATION_TYPE", 500),
    ;
}
