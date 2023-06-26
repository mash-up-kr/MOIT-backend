package com.mashup.moit.security.resolver

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
        return parameter.hasParameterAnnotation(MoitUser::class.java)
                && parameter.parameter.type == UserInfo::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any {
        val jwtAuthentication = SecurityContextHolder.getContext().authentication as JwtAuthentication
        // TODO: Cast 오류 잡기
        return jwtAuthentication.principal
    }

}
