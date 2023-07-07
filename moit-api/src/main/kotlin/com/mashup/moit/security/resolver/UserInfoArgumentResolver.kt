package com.mashup.moit.security.resolver

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.security.authentication.JwtAuthentication
import com.mashup.moit.security.authentication.UserInfo
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
        return when (val authentication = SecurityContextHolder.getContext().authentication) {
            null -> throw MoitException.of(MoitExceptionType.INVALID_USER_AUTH_TOKEN)
            is JwtAuthentication -> authentication.principal
            else -> throw MoitException.of(MoitExceptionType.INVALID_USER_AUTH_TOKEN, "The argument of GetAuth annotation is not of type UserInfo class.")
        }
    }

}
