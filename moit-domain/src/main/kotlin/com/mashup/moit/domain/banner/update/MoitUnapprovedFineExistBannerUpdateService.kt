package com.mashup.moit.domain.banner.update

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.banner.BannerEntity
import com.mashup.moit.domain.banner.BannerRepository
import com.mashup.moit.domain.banner.BannerType
import com.mashup.moit.domain.banner.update.BannerUpdateConstant.BANNER_CLOSE_MAX_DATE
import com.mashup.moit.domain.fine.FineApproveStatus
import com.mashup.moit.domain.fine.FineRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MoitUnapprovedFineExistBannerUpdateService(
    private val fineRepository: FineRepository,
    private val bannerRepository: BannerRepository,
) : BannerUpdateService {
    override fun support(request: BannerUpdateRequest): Boolean {
        return request is MoitUnapprovedFineExistBannerUpdateRequest
    }

    override fun update(request: BannerUpdateRequest) {
        if (request is MoitUnapprovedFineExistBannerUpdateRequest) {
            val fine = fineRepository.findById(request.fineId)
                .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }

            val banner = bannerRepository.findByUserIdAndMoitIdAndBannerType(
                userId = fine.userId,
                moitId = fine.moitId,
                bannerType = BannerType.MOIT_UNAPPROVED_FINE_EXIST,
            )

            val unapprovedFines = fineRepository.findAllByMoitIdAndUserIdAndApproveStatusIn(
                userId = fine.userId,
                moitId = fine.moitId,
                approveStatuses = setOf(FineApproveStatus.NEW, FineApproveStatus.IN_PROGRESS, FineApproveStatus.REJECTED),
            )

            if (unapprovedFines.isNotEmpty()) {
                if (banner !== null) {
                    if (banner.isClosed()) {
                        banner.apply {
                            this.openAt = LocalDateTime.now()
                            this.closeAt = BANNER_CLOSE_MAX_DATE
                        }
                    }
                } else {
                    bannerRepository.save(
                        BannerEntity(
                            userId = fine.userId,
                            openAt = LocalDateTime.now(),
                            closeAt = BANNER_CLOSE_MAX_DATE,
                            bannerType = BannerType.MOIT_UNAPPROVED_FINE_EXIST,
                            moitId = fine.moitId,
                            studyId = null,
                        )
                    )
                }
            } else {
                if (banner !== null && banner.isOpened()) {
                    banner.apply {
                        this.closeAt = LocalDateTime.now()
                    }
                }
            }
        } else {
            throw MoitException.of(MoitExceptionType.SYSTEM_FAIL)
        }
    }
}
