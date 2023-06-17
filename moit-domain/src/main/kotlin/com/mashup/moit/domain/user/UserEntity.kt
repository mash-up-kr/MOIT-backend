package com.mashup.moit.domain.user

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.usermoit.UserMoitEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
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
) : BaseEntity() {
    companion object {
        fun createAppleUser(appleId: Long): UserEntity = UserEntity(
            appleId = appleId,
            socialType = SocialType.APPLE
        )

        fun createKakaoUser(kakaoId: Long): UserEntity = UserEntity(
            kakaoId = kakaoId,
            socialType = SocialType.KAKAO
        )
    }
}
