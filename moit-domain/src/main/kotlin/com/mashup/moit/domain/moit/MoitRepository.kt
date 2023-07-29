package com.mashup.moit.domain.moit

import org.springframework.data.jpa.repository.JpaRepository

interface MoitRepository : JpaRepository<MoitEntity, Long> {
    fun findByInvitationCode(invitationCode: String) : MoitEntity?
}
