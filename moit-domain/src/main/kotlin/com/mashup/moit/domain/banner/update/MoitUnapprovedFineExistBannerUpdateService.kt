package com.mashup.moit.domain.banner.update

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.banner.BannerEntity
import com.mashup.moit.domain.banner.BannerRepository
import com.mashup.moit.domain.banner.BannerType
import com.mashup.moit.domain.fine.FineApproveStatus
import com.mashup.moit.domain.fine.FineRepository
import org.springframework.stereotype.Service

@Service
class MoitUnapprovedFineExistBannerUpdateService(
    private val fineRepository: FineRepository,
    private val bannerRepository: BannerRepository,
) : BannerUpdateService {
    override fun support(request: BannerUpdateRequest): Boolean {
        return request is MoitUnapprovedFineExistBannerUpdateRequest
    }

    override fun update(request: BannerUpdateRequest) {
        request as? MoitUnapprovedFineExistBannerUpdateRequest ?: throw MoitException.of(MoitExceptionType.SYSTEM_FAIL)

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
            approveStatuses = FineApproveStatus.UNAPPROVED_STATUSES,
        )

        if (unapprovedFines.isNotEmpty()) {
            if (banner != null) {
                if (banner.isClosed()) {
                    banner.open()
                }
            } else {
                bannerRepository.save(
                    BannerEntity.initializeMoitUnapprovedFineExistBanner(
                        userId = fine.userId,
                        moitId = fine.moitId,
                    )
                )
            }
        } else {
            if (banner != null && banner.isOpened()) {
                banner.close()
            }
        }
    }
}
