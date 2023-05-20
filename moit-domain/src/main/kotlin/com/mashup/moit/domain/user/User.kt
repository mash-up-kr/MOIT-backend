package com.mashup.moit.domain.user

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Entity

@Entity
class UserEntity(
    val name: String
) : BaseEntity()
