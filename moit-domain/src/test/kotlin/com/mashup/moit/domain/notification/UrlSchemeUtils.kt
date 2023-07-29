package com.mashup.moit.domain.notification

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class UrlSchemeUtilsTest {

    @Test
    fun UrlSchemeUtils_generate_test() {
        // given
        val urlScheme = "moit://test?something={something}&anything={anything}"
        val somethingSchemeParameter = UrlSchemeParameter("something", "1")
        val anythingSchemeParameter = UrlSchemeParameter("anything", "2")
        val expectedUrlScheme = "moit://test?something=1&anything=2"
        
        // when
        val updatedUrlScheme = UrlSchemeUtils.generate(
            baseScheme = urlScheme,
            somethingSchemeParameter,
            anythingSchemeParameter
        )
        
        // then
        assertThat(updatedUrlScheme).isEqualTo(expectedUrlScheme)
    }
    
}
