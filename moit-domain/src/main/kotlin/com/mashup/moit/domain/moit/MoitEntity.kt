package com.mashup.moit.domain.moit

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Table(name = "moit")
@Entity
class MoitEntity(
    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "description")
    val description: String? = null,

    @Column(name = "profile_url")
    val profileUrl: String? = null,

    @Column(name = "invitation_code", nullable = false)
    val invitationCode: String,

    @Column(name = "is_end", nullable = false)
    val isEnd: Boolean = false,

    @Embedded
    val schedulePolicy: SchedulePolicyColumns,

    @Embedded
    val finePolicy: FinePolicyColumns,

    @Embedded
    val notificationPolicy: NotificationPolicyColumns,
) : BaseEntity() {
    fun toDomain() =
        Moit(
            id = id,
            name = name,
            description = description,
            imageUrl = profileUrl,
            invitationCode = invitationCode,
            isEnd = isEnd,
            scheduleDayOfWeeks = schedulePolicy.dayOfWeeks,
            scheduleRepeatCycle = schedulePolicy.repeatCycle,
            scheduleStartDate = schedulePolicy.startDate,
            scheduleEndDate = schedulePolicy.endDate,
            scheduleStartTime = schedulePolicy.startTime,
            scheduleEndTime = schedulePolicy.endTime,
            fineLateTime = finePolicy.lateTime,
            fineLateAmount = finePolicy.lateAmount,
            fineAbsenceTime = finePolicy.absenceTime,
            fineAbsenceAmount = finePolicy.absenceAmount,
            notificationIsRemindActive = notificationPolicy.isRemindActive,
            notificationRemindOption = notificationPolicy.remindOption,
            notificationRemindLevel = notificationPolicy.remindLevel
        )

     fun validateDateForJoin(): MoitEntity {
        if (isEnd) {
            throw MoitException.of(MoitExceptionType.INVALID_ACCESS, "종료된 Moit 입니다")
        }
        if (schedulePolicy.startDate.isBefore(LocalDate.now())) {
            throw MoitException.of(MoitExceptionType.INVALID_ACCESS, "이미 시작된 Moit 입니다")
        }
        return this
    }
} 
