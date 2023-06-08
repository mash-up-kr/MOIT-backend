package com.mashup.moit.domain.user

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.usermoit.UserMoitEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Table(name = "users")
@Entity
class UserEntity(
    @Column(name = "provider_unique_key")
    val providerUniqueKey: String,
) : BaseEntity() {
    @OneToMany(mappedBy = "user")
    val attendances: List<AttendanceEntity> = emptyList()

    @OneToMany(mappedBy = "user")
    val userMoits: List<UserMoitEntity> = emptyList()

    fun toDomain(): User {
        return User(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted,
            providerUniqueKey = providerUniqueKey,
            attendances = attendances,
            userMoits = userMoits
        )
    }
    
}
