package com.mashup.moit.domain.usermoit

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "user_moit")
@Entity
class UserMoitEntity(
    @Column(name = "moit_id", nullable = false)
    val moitId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserMoitRole,
) : BaseEntity() {
    fun toDomain() =
        UserMoit(
            id = id,
            moitId = moitId,
            userId = userId,
            role = role.name
        )
}
