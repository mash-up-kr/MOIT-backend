package com.mashup.moit.facade

import com.mashup.moit.domain.user.User
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.security.OidcUser.getProviderUniqueKey
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component

@Component
class UserFacade(
    private val userService: UserService
) {

    fun findByProviderUniqueKey(oidcUser: OidcUser): User? {
        return userService.findByProviderUniqueKey(oidcUser.getProviderUniqueKey())
    }

}
