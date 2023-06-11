package com.mashup.moit.domain.user

enum class SocialType {
    KAKAO, APPLE;
}

data class User(
    val id: Long, 
    val socialType: String
)
