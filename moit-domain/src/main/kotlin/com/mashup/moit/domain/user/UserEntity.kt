package com.mashup.moit.domain.user

import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.moit.converter.UserRoleConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where

@Table(name = "users")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=?")
@Entity
class UserEntity(
    @Column(name = "provider_unique_key", nullable = false)
    val providerUniqueKey: String,

    @Column(name = "nickname", nullable = false)
    val nickname: String,

    @Column(name = "profile_image", nullable = false)
    val profileImage: Int,

    @Column(name = "email", nullable = false)
    val email: String,

    @Convert(converter = UserRoleConverter::class)
    @Column(name = "roles")
    val roles: Set<UserRole>,
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
            roles = roles,
        )
    }

    companion object {
        const val USER_ROLE_DELIMITER = "|"
    }

}
