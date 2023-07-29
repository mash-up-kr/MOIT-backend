package com.mashup.moit.facade

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.controller.dto.UserRegisterRequest
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.user.User
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.domain.usermoit.UserMoitRole
import com.mashup.moit.domain.usermoit.UserMoitService
import com.mashup.moit.security.authentication.getProviderUniqueKey
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserFacade(
    private val userService: UserService,
    private val moitService: MoitService,
    private val userMoitService: UserMoitService,
) {

    fun deleteById(userId: Long) {
        return userService.deleteUser(userId)
    }

    fun findByProviderUniqueKey(oidcUser: OidcUser): User? {
        return userService.findByProviderUniqueKey(oidcUser.getProviderUniqueKey())
    }

    @Transactional
    fun createUser(userRegisterRequest: UserRegisterRequest): User {
        if (userService.findByProviderUniqueKey(userRegisterRequest.providerUniqueKey) != null) {
            throw MoitException.of(MoitExceptionType.ALREADY_EXIST)
        }
        return userService.createUser(
            userRegisterRequest.providerUniqueKey,
            userRegisterRequest.nickname,
            userRegisterRequest.profileImage,
            userRegisterRequest.email
        ).also {
            registerMoit(it, userRegisterRequest.moitInvitationCode)
        }
    }

    private fun registerMoit(
        it: User,
        moitInvitationCode: String?
    ) {
        val userId = it.id
        moitInvitationCode?.apply {
            val moit = moitService.getMoitByInvitationCode(this)
            userMoitService.join(userId, moit.id, UserMoitRole.MEMBER)
        }
    }

}
