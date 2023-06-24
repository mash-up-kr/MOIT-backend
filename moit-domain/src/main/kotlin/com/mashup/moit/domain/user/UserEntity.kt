package com.mashup.moit.domain.user

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "users")
@Entity
class UserEntity(
    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    val socialType: SocialType,

    @Column(name = "apple_id")
    val appleId: Long? = null,

    @Column(name = "kakao_id")
    val kakaoId: Long? = null,

    @Column(name = "nickname")
    val nickname: String,
) : BaseEntity() {
    fun toDomain() = User(
        id = id,
        socialType = socialType,
        nickname = nickname
    )

    companion object {
        fun createAppleUser(appleId: Long, nickname: String): UserEntity = UserEntity(
            appleId = appleId,
            socialType = SocialType.APPLE,
            nickname = nickname
        )

        fun createKakaoUser(kakaoId: Long, nickname: String): UserEntity = UserEntity(
            kakaoId = kakaoId,
            socialType = SocialType.KAKAO,
            nickname = nickname
        )
    }
}
