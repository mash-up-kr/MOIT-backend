package com.mashup.moit.security

import com.mashup.moit.domain.user.User
import com.mashup.moit.domain.user.UserRole

data class UserInfo(
    val id: Long,
    val providerUniqueKey: String,
    val nickname: String,
    val profileImage: Int,
    val email: String,
    val roles: List<UserRole>,
) {
    companion object {
        fun from(user: User): UserInfo {
            return UserInfo(
                user.id,
                user.providerUniqueKey,
                user.nickname,
                user.profileImage,
                user.email,
                user.roles
            )
        }
    }
}
