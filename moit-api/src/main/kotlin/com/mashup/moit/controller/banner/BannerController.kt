package com.mashup.moit.controller.banner

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.controller.banner.dto.BannerListResponse
import com.mashup.moit.facade.BannerFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Banner", description = "Banner 관련 Api 입니다.")
@RequestMapping("/api/v1/banner")
@RestController
class BannerController(
    private val bannerFacade: BannerFacade,
) {
    @Operation(summary = "My Banner API", description = "내 Banner 조회")
    @GetMapping
    fun getMyBanners(
        @GetAuth userInfo: UserInfo,
        @RequestParam(required = false) moitId: Long?,
    ): MoitApiResponse<BannerListResponse> {
        return MoitApiResponse.success(bannerFacade.findOpenedBanner(userInfo.id, moitId))
    }
}
