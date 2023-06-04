package com.mashup.moit.domain.sample

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Entity

@Entity
class SampleEntity(
    val name: String,
) : BaseEntity() {
    fun toDomain() = Sample(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
    )
}
