package com.mashup.moit.domain.usermoit

import com.mashup.moit.domain.moit.MoitEntity
import com.mashup.moit.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserMoitRepository : JpaRepository<UserMoitEntity, Long> {
    fun existsByUserAndMoit(user: UserEntity, moit: MoitEntity): Boolean
}
