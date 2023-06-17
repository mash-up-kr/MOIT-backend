package com.mashup.moit.domain.study

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.moit.MoitRepository
import com.mashup.moit.domain.moit.NotificationPolicyColumns
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class StudyService(
    private val studyRepository: StudyRepository,
    private val moitRepository: MoitRepository,
) {
    @Transactional
    fun createStudies(moitId: Long) {
        val moit = moitRepository.findById(moitId).orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }

        StudyDatesCalculator.calculateStudyDates(
            dayOfWeeks = moit.schedulePolicy.dayOfWeeks,
            startDate = moit.schedulePolicy.startDate,
            endDate = moit.schedulePolicy.endDate,
            startTime = moit.schedulePolicy.startTime,
            repeatCycle = moit.schedulePolicy.repeatCycle,
        ).mapIndexed { index, studyDate ->
            val startAt = LocalDateTime.of(studyDate, moit.schedulePolicy.startTime)
            StudyEntity(
                moit = moit,
                order = index,
                startAt = startAt,
                endAt = LocalDateTime.of(studyDate, moit.schedulePolicy.endTime),
                remindAt = moit.notificationPolicy.remindAt(startAt),
                lateAt = startAt.plusMinutes(moit.finePolicy.lateTime.toLong()),
                absenceAt = startAt.plusMinutes(moit.finePolicy.absenceTime.toLong()),
            )
        }.let { studyRepository.saveAll(it) }
    }

    private fun NotificationPolicyColumns.remindAt(startAt: LocalDateTime): LocalDateTime? {
        return if (isRemindActive) {
            StudyRemindAtCalculator.calculate(startAt, remindOption!!)
        } else {
            null
        }
    }
    
    fun getAttendanceKeyword(studyId: Long): String? {
        return studyRepository.findById(studyId)
            .orElseThrow{ MoitException.of(MoitExceptionType.NOT_EXIST)}
            .attendanceCode
    }
}
