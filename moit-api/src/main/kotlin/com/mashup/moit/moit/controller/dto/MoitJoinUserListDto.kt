package com.mashup.moit.moit.controller.dto

import com.mashup.moit.domain.user.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Moit 가입 유저 리스트 응답")
data class MoitJoinUserListResponse(
    @Schema(description = "Moit 가입 유저")
    val users: List<MoitJoinUserResponse>,
) {
    companion object {
        fun of(users: List<User>, masterUserId: Long) = MoitJoinUserListResponse(
            users = users.map { MoitJoinUserResponse.of(it, it.id == masterUserId) }
        )
    }
}

@Schema(description = "Moit 가입 유저 응답")
data class MoitJoinUserResponse(
    @Schema(description = "Moit 가입 유저 아이디")
    val userId: Long,
    @Schema(description = "Moit 가입 유저 닉네임")
    val nickname: String,
    @Schema(description = "Moit 가입 유저 프로필 이미지")
    val profileImage: Int,
    @Schema(description = "Moit 가입 유저 스터디장 여부")
    val isMaster: Boolean,
) {
    companion object {
        fun of(user: User, isMaster: Boolean): MoitJoinUserResponse = MoitJoinUserResponse(
            userId = user.id,
            nickname = user.nickname,
            profileImage = user.profileImage,
            isMaster = isMaster,
        )
    }
}
