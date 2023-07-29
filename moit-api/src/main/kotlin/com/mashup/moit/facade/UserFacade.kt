package com.mashup.moit.facade

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.controller.dto.UserRegisterRequest
import com.mashup.moit.domain.user.User
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.security.authentication.getProviderUniqueKey
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserFacade(
    private val userService: UserService
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
        val user = userService.createUser(
            userRegisterRequest.providerUniqueKey,
            userRegisterRequest.nickname,
            userRegisterRequest.profileImage,
            userRegisterRequest.email
        )
        return user
    }

}
