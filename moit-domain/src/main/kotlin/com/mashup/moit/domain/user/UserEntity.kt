package com.mashup.moit.domain.user

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "users")
@Entity
class UserEntity(
    @Column(name = "provider_unique_key", nullable = false)
    val providerUniqueKey: String,

    @Column(name = "nickname", nullable = false)
    val nickname: String,

    @Column(name = "profile_image", nullable = false)
    val profileImage: Int,

    @Column(name = "email", nullable = false)
    val email: String
) : BaseEntity() {
    fun toDomain(): User {
        return User(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted,
            providerUniqueKey = providerUniqueKey,
            nickname = nickname,
            profileImage = profileImage,
            email = email,
        )
    }

}
