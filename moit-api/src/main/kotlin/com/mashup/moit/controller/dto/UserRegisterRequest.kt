package com.mashup.moit.controller.dto

data class UserRegisterRequest(
    val providerUniqueKey: String,
    val nickname: String,
    val email: String,
    val profileImage: Int,
    val moitInvitationCode: String?,
)
