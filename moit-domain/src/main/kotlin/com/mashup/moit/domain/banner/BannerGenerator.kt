package com.mashup.moit.domain.banner

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.fine.FineApproveStatus
import com.mashup.moit.domain.fine.FineRepository
import com.mashup.moit.domain.moit.MoitRepository
import com.mashup.moit.domain.study.StudyRepository
import org.springframework.stereotype.Component

@Component
class BannerGenerator(
    private val moitRepository: MoitRepository,
    private val studyRepository: StudyRepository,
    private val fineRepository: FineRepository,
) {
    fun generate(banner: BannerEntity): Banner {
        return when (banner.bannerType) {
            BannerType.STUDY_ATTENDANCE_START -> {
                val moit = moitRepository.findById(banner.moitId!!)
                    .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
                val study = studyRepository.findById(banner.studyId!!)
                    .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }

                StudyAttendanceStartBanner(
                    userId = banner.userId,
                    openAt = banner.openAt,
                    closeAt = banner.closeAt,
                    moitId = moit.id,
                    moitName = moit.name,
                    studyId = study.id,
                    studyStartAt = study.startAt,
                    studyLateAt = study.lateAt,
                    studyAbsenceAt = study.absenceAt,
                )
            }

            BannerType.MOIT_UNAPPROVED_FINE_EXIST -> {
                val moit = moitRepository.findById(banner.moitId!!)
                    .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
                val fines = fineRepository.findAllByMoitIdAndUserIdAndApproveStatusIn(
                    moitId = banner.moitId,
                    userId = banner.userId,
                    approveStatuses = setOf(FineApproveStatus.NEW, FineApproveStatus.IN_PROGRESS, FineApproveStatus.REJECTED),
                )

                MoitUnapprovedFineExistBanner(
                    userId = banner.userId,
                    openAt = banner.openAt,
                    closeAt = banner.closeAt,
                    moitId = moit.id,
                    moitName = moit.name,
                    fineAmount = fines.filter { it.approveStatus !== FineApproveStatus.APPROVED }.sumOf { it.amount },
                )
            }
        }
    }
}
