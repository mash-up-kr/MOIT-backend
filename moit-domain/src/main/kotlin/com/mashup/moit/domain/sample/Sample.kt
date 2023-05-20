package com.mashup.moit.domain.sample

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Entity

@Entity
class Sample(
    val name: String,
) : BaseEntity()
