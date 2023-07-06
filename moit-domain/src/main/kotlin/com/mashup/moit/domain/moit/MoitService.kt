package com.mashup.moit.domain.moit

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.usermoit.UserMoitRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Service
@Transactional(readOnly = true)
class MoitService(
    private val moitRepository: MoitRepository,
    private val userMoitRepository: UserMoitRepository,
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
            val randomString = RandomStringUtils.randomAlphanumeric(6).uppercase()
            if (moitRepository.findByInvitationCode(randomString) == null) {
                return randomString
            }
        }
    }
    
    fun getMoitByInvitationCode(invitationCode: String): Moit {
        return moitRepository.findByInvitationCode(invitationCode.uppercase(Locale.getDefault()))
            ?.also { it.validateDateForJoin() }?.toDomain()
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST)
    }

    fun getMoitById(moitId: Long): Moit {
        return moitRepository.findById(moitId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .toDomain()
    }

    fun getMoitsByUserId(userId: Long): List<Moit> {
        val moitIds = userMoitRepository.findAllByUserId(userId).map { it.moitId }
        return moitRepository.findAllById(moitIds).map { it.toDomain() }
    }
}
