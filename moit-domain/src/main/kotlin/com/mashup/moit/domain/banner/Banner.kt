package com.mashup.moit.domain.banner

import java.time.LocalDateTime

sealed interface Banner {
    val userId: Long
    val moitId: Long?
}

data class StudyAttendanceStartBanner(
    override val userId: Long,
    override val moitId: Long,
    val moitName: String,
    val studyId: Long,
    val studyStartAt: LocalDateTime,
    val studyLateAt: LocalDateTime,
    val studyAbsenceAt: LocalDateTime,
) : Banner

data class MoitUnapprovedFineExistBanner(
    override val userId: Long,
    override val moitId: Long,
    val moitName: String,
    val fineAmount: Long,
) : Banner

data class DefaultBanner(
    override val userId: Long,
    override val moitId: Long?,
) : Banner

enum class BannerType {
    STUDY_ATTENDANCE_START,
    MOIT_UNAPPROVED_FINE_EXIST,
    ;
}
