package com.mashup.moit.controller.user.dto

data class UserRegisterRequest(
    val providerUniqueKey: String,
    val nickname: String,
    val email: String,
    val profileImage: Int,
    val moitInvitationCode: String?,
)
