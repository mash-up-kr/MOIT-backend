package com.mashup.moit.domain.user

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Entity
class User(
    val appleId: Long? = null,
    val kakaoId: Long? = null,
    @Enumerated(EnumType.STRING) val socialType: SocialType
) : BaseEntity() {

    companion object {
        fun createAppleUser(appleId: Long): User = User(
            appleId = appleId,
            socialType = SocialType.APPLE
        )

        fun createKakaoUser(kakaoId: Long): User = User(
            kakaoId = kakaoId,
            socialType = SocialType.KAKAO
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (appleId != other.appleId) return false
        if (kakaoId != other.kakaoId) return false
        return socialType == other.socialType
    }

    override fun hashCode(): Int {
        var result = appleId?.hashCode() ?: 0
        result = 31 * result + (kakaoId?.hashCode() ?: 0)
        result = 31 * result + socialType.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(appleId=$appleId, kakaoId=$kakaoId, socialType=$socialType)"
    }
}
