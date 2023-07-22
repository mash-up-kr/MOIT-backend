package com.mashup.moit.facade

import com.mashup.moit.controller.banner.dto.BannerListResponse
import com.mashup.moit.controller.banner.dto.DefaultBannerResponse
import com.mashup.moit.controller.banner.dto.MoitUnapprovedFineExistBannerResponse
import com.mashup.moit.controller.banner.dto.StudyAttendanceStartBannerResponse
import com.mashup.moit.domain.banner.BannerService
import com.mashup.moit.domain.banner.DefaultBanner
import com.mashup.moit.domain.banner.MoitUnapprovedFineExistBanner
import com.mashup.moit.domain.banner.StudyAttendanceStartBanner
import org.springframework.stereotype.Component

@Component
class BannerFacade(
    private val bannerService: BannerService,
) {
    fun findOpenedBanner(userId: Long, moitId: Long?): BannerListResponse {
        val banners = bannerService.findOpenedBannerByUserId(userId)
        val moitFilteredBanners = if (moitId != null) banners.filter { it.moitId == moitId } else banners

        return BannerListResponse(
            studyAttendanceStartBanners = moitFilteredBanners.filterIsInstance<StudyAttendanceStartBanner>()
                .map { StudyAttendanceStartBannerResponse.of(it, 1) },
            moitUnapprovedFineExistBanners = moitFilteredBanners.filterIsInstance<MoitUnapprovedFineExistBanner>()
                .map { MoitUnapprovedFineExistBannerResponse.of(it, 1) },
            defaultBanner = moitFilteredBanners.filterIsInstance<DefaultBanner>()
                .map { DefaultBannerResponse.of(it) },
        )
    }
}
