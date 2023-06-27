package com.mashup.moit.security.resolver

import com.mashup.moit.security.authentication.JwtAuthentication
import com.mashup.moit.security.authentication.UserInfo
import jakarta.validation.ValidationException
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserInfoArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(GetAuth::class.java)
                && parameter.parameter.type == UserInfo::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any {
        val authentication = SecurityContextHolder.getContext().authentication
        return when(authentication) {
            is JwtAuthentication -> authentication.principal
            else -> throw ValidationException("The argument of GetAuth annotation is not of type UserInfo class.")
        }
    }

}
