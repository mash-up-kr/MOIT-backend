package com.mashup.moit.security

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.controller.dto.UserBeforeSignUpInfoResponse
import org.springframework.security.oauth2.core.oidc.user.OidcUser

private const val PROVIDER_SPLIT_DELIMITER = "|"
private const val CLAIM_SUB_KEY = "sub"
private const val CLAIM_EMAIL_KEY = "email"
private const val CLAIM_NICKNAME_KEY = "nickname"


fun OidcUser.getAuthProvider(): String {
    val jwtClaim = this.getClaimAsString(CLAIM_SUB_KEY)
    val lastIndex = jwtClaim.lastIndexOf(PROVIDER_SPLIT_DELIMITER)
    return jwtClaim.takeIf { lastIndex > 0 }?.substring(0, lastIndex)
        ?: throw MoitException.of(MoitExceptionType.INVALID_AUTH_PROVIDER)
}

fun OidcUser.getProviderUniqueKey(): String {
    val email = this.getClaimAsString(CLAIM_EMAIL_KEY)
    val authProvider = this.getAuthProvider()
    return "$authProvider${PROVIDER_SPLIT_DELIMITER}$email"
}

fun OidcUser.toBeforeSignUpInfo(): UserBeforeSignUpInfoResponse {
    val email = this.getClaimAsString(CLAIM_EMAIL_KEY)
    val nickname = this.getClaimAsString(CLAIM_NICKNAME_KEY)
    val authProviderUniqueKey = this.getProviderUniqueKey()
    return UserBeforeSignUpInfoResponse(authProviderUniqueKey, nickname, email)
}

