package com.mashup.moit.security

import com.mashup.moit.domain.sample.SampleUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(private val user: SampleUser): Authentication {
    
    private var authenticated: Boolean = true
    
    override fun getName(): String {
        return user.nickname
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("User role 추가") 
    }

    override fun getCredentials(): Any {
        TODO("Not yet implemented")
    }

    override fun getDetails(): Any {
        return user.email
    }

    override fun getPrincipal(): Any {
        return user
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(authenticated: Boolean) {
        this.authenticated = authenticated
    }
    
}
