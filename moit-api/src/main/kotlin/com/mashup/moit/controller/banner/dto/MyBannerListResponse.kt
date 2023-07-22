package com.mashup.moit.controller.banner.dto

import com.mashup.moit.domain.banner.DefaultBanner
import com.mashup.moit.domain.banner.MoitUnapprovedFineExistBanner
import com.mashup.moit.domain.banner.StudyAttendanceStartBanner
import java.time.LocalDateTime

data class BannerListResponse(
    val studyAttendanceStartBanners: List<StudyAttendanceStartBannerResponse>,
    val moitUnapprovedFineExistBanners: List<MoitUnapprovedFineExistBannerResponse>,
    val defaultBanner: List<DefaultBannerResponse>,
)

data class StudyAttendanceStartBannerResponse(
    val userId: Long,
    val moitId: Long,
    val moitName: String,
    val studyId: Long,
    val studyStartAt: LocalDateTime,
    val studyLateAt: LocalDateTime,
    val studyAbsenceAt: LocalDateTime,
    val order: Int,
) {
    companion object {
        fun of(banner: StudyAttendanceStartBanner, order: Int): StudyAttendanceStartBannerResponse {
            return StudyAttendanceStartBannerResponse(
                userId = banner.userId,
                moitId = banner.moitId,
                moitName = banner.moitName,
                studyId = banner.studyId,
                studyStartAt = banner.studyStartAt,
                studyLateAt = banner.studyLateAt,
                studyAbsenceAt = banner.studyAbsenceAt,
                order = order,
            )
        }
    }
}

data class MoitUnapprovedFineExistBannerResponse(
    val userId: Long,
    val moitId: Long,
    val moitName: String,
    val findAmount: Long,
    val order: Int,
) {
    companion object {
        fun of(banner: MoitUnapprovedFineExistBanner, order: Int): MoitUnapprovedFineExistBannerResponse {
            return MoitUnapprovedFineExistBannerResponse(
                userId = banner.userId,
                moitId = banner.moitId,
                moitName = banner.moitName,
                findAmount = banner.fineAmount,
                order = order,
            )
        }
    }
}

data class DefaultBannerResponse(
    val userId: Long,
) {
    companion object {
        fun of(banner: DefaultBanner): DefaultBannerResponse {
            return DefaultBannerResponse(
                userId = banner.userId,
            )
        }
    }
}
