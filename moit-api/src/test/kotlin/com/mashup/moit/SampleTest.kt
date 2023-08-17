package com.mashup.moit

import com.mashup.moit.domain.banner.update.MoitUnapprovedFineExistBannerUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SampleTest {
    
    @Test
    fun test() {
        val updateRequest1 = MoitUnapprovedFineExistBannerUpdateRequest(1)
        val updateRequest2 = MoitUnapprovedFineExistBannerUpdateRequest(1)
        
        assertThat(updateRequest1).isEqualTo(updateRequest2)
        println(updateRequest1 == updateRequest2)
        println(updateRequest1.equals(updateRequest2))
    }
    
}
