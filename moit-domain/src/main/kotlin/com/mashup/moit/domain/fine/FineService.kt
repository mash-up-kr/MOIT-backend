package com.mashup.moit.domain.fine

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.attendance.AttendanceRepository
import com.mashup.moit.domain.attendance.AttendanceStatus
import com.mashup.moit.domain.moit.MoitRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FineService(
    private val fineRepository: FineRepository,
    private val attendanceRepository: AttendanceRepository,
    private val moitRepository: MoitRepository,
) {
    @Transactional
    fun create(attendanceId: Long, moitId: Long): Fine? {
        val attendance = attendanceRepository.findById(attendanceId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
        val moit = moitRepository.findById(moitId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }

        val fineAmount = when (attendance.status) {
            AttendanceStatus.LATE -> moit.finePolicy.lateAmount
            AttendanceStatus.ABSENCE -> moit.finePolicy.absenceAmount
            else -> return null
        }

        return fineRepository.save(
            FineEntity(
                attendance = attendance,
                amount = fineAmount,
                approveStatus = FineApproveStatus.NEW,
                approvedAt = null,
                paymentImageUrl = null,
                userId = attendance.userId,
                moitId = moit.id,
                studyId = attendance.studyId,
            )
        ).toDomain()
    }

    fun getFine(fineId: Long): Fine {
        return fineRepository.findById(fineId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .toDomain()
    }

    fun getFineListByMoitId(moitId: Long): List<Fine> {
        return fineRepository.findByMoitId(moitId)
            .sortedByDescending { it.createdAt }
            .map { it.toDomain() }
    }

    @Transactional
    fun updateFineApproveStatus(fineId: Long, confirmFine: Boolean) {
        val approveStatus = if (confirmFine) FineApproveStatus.APPROVED else FineApproveStatus.REJECTED
        fineRepository.findById(fineId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .apply { this.approveStatus = approveStatus }
    }

    @Transactional
    fun addFinePaymentImage(userId: Long, fineId: Long, finePaymentImageUrl: String): Fine {
        return fineRepository.findById(fineId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .takeIf { it.userId == userId }
            ?.apply {
                this.paymentImageUrl = finePaymentImageUrl
                this.approveStatus = FineApproveStatus.IN_PROGRESS
            }
            ?.toDomain()
            ?: throw MoitException.of(MoitExceptionType.INVALID_ACCESS)
    }

}
