package com.mashup.moit.controller.banner.dto

import com.mashup.moit.domain.banner.DefaultBanner
import com.mashup.moit.domain.banner.MoitUnapprovedFineExistBanner
import com.mashup.moit.domain.banner.StudyAttendanceStartBanner
import java.time.LocalDateTime

data class BannerListResponse(
    val studyAttendanceStartBanners: List<StudyAttendanceStartBannerResponse>,
    val moitUnapprovedFineExistBanners: List<MoitUnapprovedFineExistBannerResponse>,
    val defaultBanners: List<DefaultBannerResponse>,
)

data class StudyAttendanceStartBannerResponse(
    val userId: Long,
    val moitId: Long,
    val moitName: String,
    val studyId: Long,
    val studyStartAt: LocalDateTime,
    val studyLateAt: LocalDateTime,
    val studyAbsenceAt: LocalDateTime,
) {
    companion object {
        fun of(banner: StudyAttendanceStartBanner): StudyAttendanceStartBannerResponse {
            return StudyAttendanceStartBannerResponse(
                userId = banner.userId,
                moitId = banner.moitId,
                moitName = banner.moitName,
                studyId = banner.studyId,
                studyStartAt = banner.studyStartAt,
                studyLateAt = banner.studyLateAt,
                studyAbsenceAt = banner.studyAbsenceAt,
            )
        }
    }
}

data class MoitUnapprovedFineExistBannerResponse(
    val userId: Long,
    val moitId: Long,
    val moitName: String,
    val findAmount: Long,
) {
    companion object {
        fun of(banner: MoitUnapprovedFineExistBanner): MoitUnapprovedFineExistBannerResponse {
            return MoitUnapprovedFineExistBannerResponse(
                userId = banner.userId,
                moitId = banner.moitId,
                moitName = banner.moitName,
                findAmount = banner.fineAmount,
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
