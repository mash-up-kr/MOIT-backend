package com.mashup.moit.controller.user.dto

data class UserBeforeSignUpInfoResponse(
    val providerUniqueKey: String,
    val nickname: String,
    val email: String
)
