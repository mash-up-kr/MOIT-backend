package com.mashup.moit.domain.usermoit

import org.springframework.data.jpa.repository.JpaRepository

interface UserMoitRepository : JpaRepository<UserMoitEntity, Long> {
    fun existsByUserIdAndMoitId(userId: Long, moitId: Long): Boolean
    fun findAllByUserId(userId: Long): List<UserMoitEntity>
    fun findMasterByMoitIdAndRole(moitId: Long, role: UserMoitRole): UserMoitEntity?
}
