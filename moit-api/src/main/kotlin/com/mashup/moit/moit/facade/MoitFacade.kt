package com.mashup.moit.moit.facade

import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.usermoit.UserMoitService
import com.mashup.moit.moit.controller.dto.MoitJoinResponse
import org.springframework.stereotype.Component

@Component
class MoitFacade(
    private val moitService: MoitService,
    private val userMoitService: UserMoitService
) {
    fun join(userId: Long, invitationCode: String): MoitJoinResponse {
        val moit = getMoitByInvitationCode(invitationCode)
        return userMoitService.join(userId, moit.id).let { MoitJoinResponse.of(moit, it) }
    }

    private fun getMoitByInvitationCode(invitationCode: String): Moit {
        return moitService.getMoitByInvitationCode(invitationCode)
    }
}
