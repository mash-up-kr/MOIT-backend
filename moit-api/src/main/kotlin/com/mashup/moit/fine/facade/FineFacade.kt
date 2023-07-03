package com.mashup.moit.fine.facade

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.domain.usermoit.UserMoitService
import com.mashup.moit.fine.controller.dto.FineListResponse
import com.mashup.moit.fine.controller.dto.FineResponseForListView
import org.springframework.stereotype.Component

@Component
class FineFacade(
    private val fineService: FineService,
    private val studyService: StudyService,
    private val userService: UserService,
    private val userMoitService: UserMoitService,
) {
    
    fun getFineList(moitId: Long): FineListResponse {
        val fineList = fineService.getFineListByMoitId(moitId)
        if (fineList.isEmpty()) {
            return FineListResponse.emptyFineList()
        }

        val fineUserIds = fineList.map { it.userId }.distinct()
        val fineUsersMap = userService.findUsersById(fineUserIds).associateBy { it.id }

        val fineStudyIds = fineList.map { it.studyId }.distinct()
        val fineStudyMap = studyService.findByStudyIds(fineStudyIds).associateBy { it.id }

        val totalFineAmount = fineList.sumOf { it.amount }
        val (fineComplete, fineNotYet) = fineList.partition { it.isApproved }
            .let { (approvedFineList, isNotApprovedFineList) ->
                approvedFineList.map {
                    FineResponseForListView.of(it, fineUsersMap.getValue(it.userId), fineStudyMap.getValue(it.studyId))
                } to isNotApprovedFineList.map {
                    FineResponseForListView.of(it, fineUsersMap.getValue(it.userId), fineStudyMap.getValue(it.studyId))
                }
            }

        return FineListResponse(totalFineAmount, fineNotYet, fineComplete)
    }
    
    fun evaluateFine(userId: Long, moitId: Long, findId: Long, confirmFine: Boolean) {
        val masterId = userMoitService.findMasterUserByMoitId(moitId).userId
        if (userId != masterId) {
            throw MoitException.of(MoitExceptionType.ONLY_MOIT_MASTER)
        }
        
        fineService.updateFineApproveStatus(findId, confirmFine)
    }
    
}
