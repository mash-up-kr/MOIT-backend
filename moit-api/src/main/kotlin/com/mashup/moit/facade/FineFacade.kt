package com.mashup.moit.facade

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.controller.fine.dto.FineListResponse
import com.mashup.moit.controller.fine.dto.FineCertificateResponse
import com.mashup.moit.controller.fine.dto.FineResponse
import com.mashup.moit.controller.fine.dto.FineResponseForListView
import com.mashup.moit.domain.fine.FineService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.domain.usermoit.UserMoitService
import com.mashup.moit.infra.aws.s3.S3Service
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.FineApproveEvent
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class FineFacade(
    private val fineService: FineService,
    private val studyService: StudyService,
    private val userService: UserService,
    private val userMoitService: UserMoitService,
    private val eventProducer: EventProducer,
    private val s3Service: S3Service,
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

    fun getFine(fineId: Long, userNickname: String): FineResponse {
        val fine = fineService.getFine(fineId)
        return FineResponse.of(
            fine = fineService.getFine(fineId),
            study = studyService.findById(fine.studyId),
            userNickname = userNickname,
        )
    }

    fun evaluateFine(userId: Long, moitId: Long, fineId: Long, confirmFine: Boolean) {
        val masterId = userMoitService.findMasterUserByMoitId(moitId).userId
        if (userId != masterId) {
            throw MoitException.of(MoitExceptionType.ONLY_MOIT_MASTER)
        }

        fineService.updateFineApproveStatus(fineId, confirmFine)
        if (confirmFine) {
            eventProducer.produce(FineApproveEvent(fineId = fineId))
        }
    }

    fun addFineCertification(userId: Long, userNickname: String, fineId: Long, finePaymentImage: MultipartFile): FineCertificateResponse {
        val finePaymentImageUrl = s3Service.upload(FINE_PAYMENT_IMAGE_DIRECTORY, finePaymentImage)
        return FineCertificateResponse.of(fineService.addFinePaymentImage(userId, fineId, finePaymentImageUrl), userNickname)
    }

    companion object {
        private const val FINE_PAYMENT_IMAGE_DIRECTORY = "fine-payment/"
    }

}
