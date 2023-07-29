package com.mashup.moit.domain.notification

object UrlSchemeUtils {
    fun generate(baseScheme: String, vararg parameter: UrlSchemeParameter): String {
        var urlScheme: String = baseScheme
        parameter.forEach {
            urlScheme = urlScheme.replaceFirst("{${it.key}}", it.value)
        }
        return urlScheme
    }
}


data class UrlSchemeParameter(
    val key: String,
    val value: String,
)
