package com.mashup.moit.domain.moit

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class MoitService(
    private val moitRepository: MoitRepository
) {
    @Transactional
    fun createMoit(
        name: String,
        description: String?,
        dayOfWeeks: Set<DayOfWeek>,
        startDate: LocalDate,
        endDate: LocalDate,
        repeatCycle: ScheduleRepeatCycle,
        startTime: LocalTime,
        endTime: LocalTime,
        lateTime: Int,
        lateAmount: Int,
        absenceTime: Int,
        absenceAmount: Int,
        isRemindActive: Boolean,
        remindOption: NotificationRemindOption?,
    ): Moit {
        return moitRepository.save(
            MoitEntity(
                name = name,
                description = description,
                invitationCode = generateInvitationCode(),
                schedulePolicy = SchedulePolicyColumns(
                    dayOfWeeks = dayOfWeeks,
                    startDate = startDate,
                    endDate = endDate,
                    repeatCycle = repeatCycle,
                    startTime = startTime,
                    endTime = endTime,
                ),
                finePolicy = FinePolicyColumns(
                    lateTime = lateTime,
                    lateAmount = lateAmount,
                    absenceTime = absenceTime,
                    absenceAmount = absenceAmount,
                ),
                notificationPolicy = NotificationPolicyColumns(
                    isRemindActive = isRemindActive,
                    remindOption = remindOption,
                    remindLevel = NotificationRemindLevel.NORMAL,
                )
            )
        ).toDomain()
    }

    private fun generateInvitationCode(): String {
        while (true) {
            val randomString = RandomStringUtils.randomAlphanumeric(6)
            if (moitRepository.findByInvitationCode(randomString) == null) {
                return randomString
            }
        }
    }

    fun getMoitByInvitationCode(invitationCode: String): Moit {
        val moit = (moitRepository.findByInvitationCode(invitationCode.uppercase(Locale.getDefault()))
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST))

        if (moit.isEnd) throw MoitException.of(MoitExceptionType.INVALID_ACCESS, "종료된 Moit 입니다")

        return moit.toDomain()
    }
}
