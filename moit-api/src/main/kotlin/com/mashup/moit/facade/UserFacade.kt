package com.mashup.moit.facade

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.controller.dto.UserRegisterRequest
import com.mashup.moit.domain.user.User
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.security.authentication.getProviderUniqueKey
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component

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

    fun createUser(userRegisterRequest: UserRegisterRequest): User {
        if (userService.findByProviderUniqueKey(userRegisterRequest.providerUniqueKey) != null) {
            throw MoitException.of(MoitExceptionType.ALREADY_EXIST)
        }
        return userService.createUser(
            userRegisterRequest.providerUniqueKey,
            userRegisterRequest.nickname,
            userRegisterRequest.profileImage,
            userRegisterRequest.email
        )
    }

}
