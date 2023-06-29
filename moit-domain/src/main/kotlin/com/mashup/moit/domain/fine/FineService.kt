package com.mashup.moit.domain.fine

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FineService(
    private val fineRepository: FineRepository
) {
    fun getFineListByMoitId(moitId: Long): List<Fine> {
        return fineRepository.findByMoitId(moitId)
            .sortedByDescending { it.createdAt }
            .map { it.toDomain() }
    }

    @Transactional
    fun updateFineApproveStatus(findId: Long, confirmFine: Boolean) {
        val approveStatus = if (confirmFine) FineApproveStatus.APPROVED else FineApproveStatus.REJECTED
        fineRepository.findById(findId)
            .orElseThrow { MoitException.of(MoitExceptionType.NOT_EXIST) }
            .apply { this.approveStatus = approveStatus }
    }

}
