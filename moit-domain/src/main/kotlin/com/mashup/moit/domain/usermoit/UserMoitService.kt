package com.mashup.moit.domain.usermoit

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.moit.MoitRepository
import com.mashup.moit.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserMoitService(
    private val userMoitRepository: UserMoitRepository,
    private val moitRepository: MoitRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun join(userId: Long, moitId: Long): UserMoit {
        val user = userRepository.findById(userId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
        val moit = moitRepository.findById(moitId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }

        if (userMoitRepository.existsByUserAndMoit(user, moit)) {
            throw MoitException.of(MoitExceptionType.EXIST_ENTITY)
        }

        val userMoit = UserMoitEntity(moit, user, UserMoitRole.MEMBER)
        return userMoitRepository.save(userMoit).toDomain()
    }
}
