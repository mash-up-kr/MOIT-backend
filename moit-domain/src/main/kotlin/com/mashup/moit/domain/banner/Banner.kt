package com.mashup.moit.domain.banner

import java.time.LocalDateTime

sealed interface Banner {
    val userId: Long
    val openAt: LocalDateTime
    val closeAt: LocalDateTime?
    val priority: Int
}

data class StudyAttendanceStartBanner(
    override val userId: Long,
    override val openAt: LocalDateTime,
    override val closeAt: LocalDateTime?,
    val moitId: Long,
    val moitName: String,
    val studyId: Long,
    val studyStartAt: LocalDateTime,
    val studyLateAt: LocalDateTime,
    val studyAbsenceAt: LocalDateTime,
) : Banner {
    override val priority = 0
}

data class MoitUnapprovedFineExistBanner(
    override val userId: Long,
    override val openAt: LocalDateTime,
    override val closeAt: LocalDateTime?,
    val moitId: Long,
    val moitName: String,
    val fineAmount: Long,
) : Banner {
    override val priority = 1
}

data class DefaultBanner(
    override val userId: Long,
    override val openAt: LocalDateTime,
    override val closeAt: LocalDateTime?,
) : Banner {
    override val priority = 2
}

enum class BannerType {
    STUDY_ATTENDANCE_START,
    MOIT_UNAPPROVED_FINE_EXIST,
    ;
}
