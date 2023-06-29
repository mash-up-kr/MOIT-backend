package com.mashup.moit.controller.dto

data class UserBeforeSignUpInfoResponse(
    val providerUniqueKey: String,
    val nickname: String,
    val email: String
)
