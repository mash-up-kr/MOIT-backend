package com.mashup.moit.security.resolver

import io.swagger.v3.oas.annotations.media.Schema

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@Schema(hidden = true)
annotation class GetAuth
