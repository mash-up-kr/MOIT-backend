package com.mashup.moit.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByIdIn(ids: List<Long>): List<UserEntity>
}
