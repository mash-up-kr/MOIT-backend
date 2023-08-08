package com.mashup.moit.domain.study

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.moit.MoitRepository
import com.mashup.moit.domain.moit.NotificationPolicyColumns
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@Transactional(readOnly = true)
class StudyService(
    private val studyRepository: StudyRepository,
    private val moitRepository: MoitRepository,
) {
    fun findById(studyId: Long): Study {
        return studyRepository.findById(studyId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .toDomain()
    }

    fun findByStudyIds(ids: List<Long>): List<Study> {
        return studyRepository.findAllById(ids).map { it.toDomain() }
    }

    fun findUninitializedStudyStartAtBefore(startAt: LocalDateTime): List<Study> {
        return studyRepository.findAllByStartAtBeforeAndIsInitializedFalse(startAt)
            .map { it.toDomain() }
    }

    @Transactional
    fun markAsInitialized(studyId: Long): Study {
        return studyRepository.findById(studyId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .apply { this.isInitialized = true }
            .toDomain()
    }

    @Transactional
    fun markAsPushed(studyId: Long): Study {
        return studyRepository.findById(studyId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .apply { this.isPushed = true }
            .toDomain()
    }

    fun findAllByMoitIdStartAtBefore(moitId: Long, startAt: LocalDateTime): List<Study> {
        return studyRepository.findAllByMoitIdAndStartAtBeforeOrderByOrderDesc(moitId, startAt)
            .map { it.toDomain() }
    }

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
                moitId = moit.id,
                order = index,
                startAt = startAt,
                endAt = LocalDateTime.of(studyDate, moit.schedulePolicy.endTime),
                remindAt = moit.notificationPolicy.remindAt(startAt),
                lateAt = startAt.plusMinutes(moit.finePolicy.lateTime.toLong()),
                absenceAt = startAt.plusMinutes(moit.finePolicy.absenceTime.toLong()),
            )
        }.let { studyRepository.saveAll(it) }
    }

    fun findUpcomingStudy(moitId: Long): Study? {
        return studyRepository.findFirstByMoitIdAndEndAtAfterOrderByStartAtAsc(
            moitId = moitId,
            endAt = LocalDateTime.now(),
        )?.toDomain()
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
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .attendanceCode
    }

    @Transactional
    fun registerAttendanceKeyword(studyId: Long, attendanceCode: String): Study {
        return studyRepository.findById(studyId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST, "해당 스터디가 존재하지 않습니다") }
            .takeIf { study -> study.attendanceCode == null }
            ?.apply { this.attendanceCode = attendanceCode }
            ?.toDomain()
            ?: throw MoitException.of(MoitExceptionType.ALREADY_EXIST, "출석 키워드가 이미 존재합니다")
    }

    fun verifyAttendanceKeyword(studyId: Long, attendanceCode: String): Study {
        val study = studyRepository.findById(studyId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST, "해당 스터디가 존재하지 않습니다") }
        when (study.attendanceCode) {
            attendanceCode -> Unit
            null -> throw MoitException.of(MoitExceptionType.NOT_EXIST, "출석 키워드가 등록되지 않았습니다")
            else -> throw MoitException.of(MoitExceptionType.INVALID_ATTENDANCE_KEYWORD)
        }
        return study.toDomain()
    }

    fun findStudiesByStartTime(min: LocalDateTime, max: LocalDateTime): List<Study> {
        return studyRepository.findAllByStartAtGreaterThanEqualAndStartAtLessThanEqual(min, max)
            .filter { !it.isInitialized } // 모종의 initialize 실패 인입 방지 
            .map { it.toDomain() }
    }

    fun findUnfinalizedStudiesByEndAtBefore(endAt: LocalDateTime): List<Study> {
        return studyRepository.findAllByEndAtBeforeAndIsFinalizedFalse(endAt)
            .map { it.toDomain() }
    }

    fun findNotPushedStudies(basedTime: LocalDateTime): List<Study> {
        return studyRepository.findByPushedAndRemindAtBefore(false, basedTime)
            .map { it.toDomain() }
    }
}
