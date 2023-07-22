package com.mashup.moit.domain.banner

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BannerRepository : JpaRepository<BannerEntity, Long> {
    // findBy UserId And OpenAtAfter And CloseAtBefore
    fun findByUserIdAndOpenAtAfterAndCloseAtBefore(userId: Long, openAt: LocalDateTime, closeAt: LocalDateTime): List<BannerEntity>

    // findBy UserId In And StudyId And BannerType
    fun findByUserIdInAndStudyIdAndBannerType(userIds: Collection<Long>, studyId: Long, bannerType: BannerType): List<BannerEntity>

    // findBy UserId And MoitId And BannerType
    fun findByUserIdAndMoitIdAndBannerType(userId: Long, moitId: Long, bannerType: BannerType): BannerEntity?
}
