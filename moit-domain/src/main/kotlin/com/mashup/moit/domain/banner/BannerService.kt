package com.mashup.moit.domain.banner

import com.mashup.moit.domain.banner.update.BannerUpdateRequest
import com.mashup.moit.domain.banner.update.BannerUpdateService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BannerService(
    private val bannerRepository: BannerRepository,
    private val bannerGenerator: BannerGenerator,
    private val bannerUpdateServices: List<BannerUpdateService>,
) {
    fun findOpenedBannerByUserId(userId: Long): List<Banner> {
        val now = LocalDateTime.now()
        val banners = bannerRepository.findByUserIdAndOpenAtAfterAndCloseAtBefore(
            userId = userId,
            openAt = now,
            closeAt = now,
        ).map { bannerGenerator.generate(it) }

        return banners.ifEmpty {
            listOf(
                DefaultBanner(
                    userId = userId,
                    moitId = null,
                )
            )
        }
    }

    @Transactional
    fun update(request: BannerUpdateRequest) {
        bannerUpdateServices
            .filter { it.support(request) }
            .forEach { it.update(request) }
    }
}
