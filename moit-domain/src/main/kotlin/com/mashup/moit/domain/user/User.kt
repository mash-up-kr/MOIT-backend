package com.mashup.moit.domain.user

import java.time.LocalDateTime

data class User(
    val id: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isDeleted: Boolean,
    val providerUniqueKey: String,
    val nickname: String,
    val profileImage: Int,
    val email: String,
    val roles: Set<UserRole>,
)
