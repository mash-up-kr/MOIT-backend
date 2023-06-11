package com.mashup.moit.domain.moit

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class MoitService(
    private val moitRepository: MoitRepository
) {
    fun getMoitByInvitationCode(invitationCode: String): Moit {
        val moit = (moitRepository.findByInvitationCode(invitationCode.uppercase(Locale.getDefault()))
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST))

        if (moit.isEnd) throw MoitException.of(MoitExceptionType.INVALID_ACCESS, "종료된 Moit 입니다")

        return moit.toDomain()
    }
}
