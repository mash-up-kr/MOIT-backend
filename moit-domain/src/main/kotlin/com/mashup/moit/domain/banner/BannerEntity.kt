package com.mashup.moit.domain.banner

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Table(name = "banner")
@Entity
class BannerEntity(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "open_at", nullable = false)
    var openAt: LocalDateTime,

    @Column(name = "close_at")
    var closeAt: LocalDateTime?,

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
        return openAt.isBefore(now) && (closeAt == null || closeAt!!.isAfter(now))
    }

    fun isClosed(): Boolean {
        return !isOpened()
    }
}
