package com.mashup.moit.domain.banner

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Table(name = "banner")
@Entity
class BannerEntity(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "open_at", nullable = false)
    var openAt: LocalDateTime,

    @Column(name = "close_at", nullable = false)
    var closeAt: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "banner_type", nullable = false)
    val bannerType: BannerType,

    @Column(name = "moid_id")
    val moitId: Long?,

    @Column(name = "study_id")
    val studyId: Long?,
) : BaseEntity() {
    fun isOpened(): Boolean {
        val now = LocalDateTime.now()
        return openAt.let { it.isBefore(now) || it.isEqual(now) } && closeAt.isAfter(now)
    }

    fun isClosed(): Boolean {
        return !isOpened()
    }

    fun open() {
        this.openAt = LocalDateTime.now()
        this.closeAt = BANNER_CLOSE_MAX_DATETIME
    }

    fun close() {
        this.closeAt = LocalDateTime.now()
    }

    companion object {
        val BANNER_CLOSE_MAX_DATETIME: LocalDateTime = LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.of(23, 59, 59))

        fun initializeStudyAttendanceStartBanner(
            userId: Long,
            openAt: LocalDateTime,
            closeAt: LocalDateTime,
            moitId: Long,
            studyId: Long,
        ) = BannerEntity(
            userId = userId,
            openAt = openAt,
            closeAt = closeAt,
            bannerType = BannerType.STUDY_ATTENDANCE_START,
            moitId = moitId,
            studyId = studyId,
        )

        fun initializeMoitUnapprovedFineExistBanner(
            userId: Long,
            moitId: Long,
        ) = BannerEntity(
            userId = userId,
            openAt = LocalDateTime.now(),
            closeAt = BANNER_CLOSE_MAX_DATETIME,
            bannerType = BannerType.MOIT_UNAPPROVED_FINE_EXIST,
            moitId = moitId,
            studyId = null,
        )
    }
}
