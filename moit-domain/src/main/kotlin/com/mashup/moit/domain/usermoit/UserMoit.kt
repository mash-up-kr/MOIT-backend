package com.mashup.moit.domain.usermoit

data class UserMoit(
    val id: Long,
    val userId: Long,
    val moitId: Long,
    val role: String,
)
