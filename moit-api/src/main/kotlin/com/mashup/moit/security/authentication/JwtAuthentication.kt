package com.mashup.moit.security.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(private val moitUser: MoitUser) : Authentication {
    private var authenticated: Boolean = true

    override fun getName(): String {
        return moitUser.name
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return moitUser.authorities
    }

    override fun getCredentials(): Any {
        return moitUser.userInfo.id
    }

    override fun getDetails(): Any {
        return moitUser.attributes
    }

    override fun getPrincipal(): Any {
        return moitUser
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(authenticated: Boolean) {
        this.authenticated = authenticated
    }

}
