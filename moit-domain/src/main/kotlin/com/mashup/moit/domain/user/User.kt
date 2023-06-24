package com.mashup.moit.domain.user

enum class SocialType {
    KAKAO, APPLE;
}

data class User(
    val id: Long,
    val socialType: SocialType,
    // TODO UserEntity 에 먼저 추가 필요 (인증 작업 후 진행 필요)
    val nickname: String,
    val profileImage: Int = 1,
)
