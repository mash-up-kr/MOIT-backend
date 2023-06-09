package com.mashup.moit.domain.usermoit

import org.springframework.data.jpa.repository.JpaRepository

interface UserMoitRepository : JpaRepository<UserMoitEntity, Long> {
    fun existsByUserIdAndMoitId(userId: Long, moitId: Long): Boolean
    fun findByMoitIdAndRole(moitId: Long, role: UserMoitRole): UserMoitEntity?
    fun findAllByMoitId(moitId: Long): List<UserMoitEntity>
    fun findAllByUserId(userId: Long): List<UserMoitEntity>
}
