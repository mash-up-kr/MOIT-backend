package com.mashup.moit.domain.banner.update

interface BannerUpdateService {
    fun support(request: BannerUpdateRequest): Boolean
    fun update(request: BannerUpdateRequest)
}
