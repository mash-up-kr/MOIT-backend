package com.mashup.moit.domain.banner.update

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.banner.BannerEntity
import com.mashup.moit.domain.banner.BannerRepository
import com.mashup.moit.domain.banner.BannerType
import com.mashup.moit.domain.study.StudyRepository
import com.mashup.moit.domain.usermoit.UserMoitRepository
import org.springframework.stereotype.Service

@Service
class StudyAttendanceStartBannerUpdateService(
    private val studyRepository: StudyRepository,
    private val userMoitRepository: UserMoitRepository,
    private val bannerRepository: BannerRepository,
) : BannerUpdateService {
    override fun support(request: BannerUpdateRequest): Boolean {
        return request is StudyAttendanceStartBannerUpdateRequest
    }

    override fun update(request: BannerUpdateRequest) {
        if (request is StudyAttendanceStartBannerUpdateRequest) {
            val study = studyRepository.findById(request.studyId)
                .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            val studyUserIds = userMoitRepository.findAllByMoitId(study.moitId).map { it.userId }

            val existedBannerUserIds = bannerRepository.findByUserIdInAndStudyIdAndBannerType(
                userIds = studyUserIds,
                studyId = study.id,
                bannerType = BannerType.STUDY_ATTENDANCE_START,
            ).map { it.userId }

            studyUserIds.filter { it !in existedBannerUserIds }
                .map {
                    BannerEntity(
                        userId = it,
                        openAt = study.startAt.minusMinutes(10L),
                        closeAt = study.endAt,
                        bannerType = BannerType.STUDY_ATTENDANCE_START,
                        moitId = study.moitId,
                        studyId = study.id,
                    )
                }.let { bannerRepository.saveAll(it) }
        } else {
            throw MoitException.of(MoitExceptionType.SYSTEM_FAIL)
        }
    }
}
