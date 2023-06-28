package com.mashup.moit.moit.facade

import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.usermoit.UserMoitRole
import com.mashup.moit.domain.usermoit.UserMoitService
import com.mashup.moit.moit.controller.dto.MoitCreateRequest
import com.mashup.moit.moit.controller.dto.MoitDetailsResponse
import com.mashup.moit.moit.controller.dto.MoitJoinResponse
import com.mashup.moit.moit.controller.dto.MyMoitListResponse
import com.mashup.moit.moit.controller.dto.MyMoitResponseForListView
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate
import java.time.Period

@Component
class MoitFacade(
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val userMoitService: UserMoitService
) {
    @Transactional
    fun create(userId: Long, request: MoitCreateRequest): Long {
        val moit = moitService.createMoit(
            name = request.name,
            description = request.description,
            dayOfWeeks = request.dayOfWeeks,
            startDate = request.startDate,
            endDate = request.endDate,
            repeatCycle = request.repeatCycle,
            startTime = request.startTime,
            endTime = request.endTime,
            lateTime = request.lateTime,
            lateAmount = request.lateAmount,
            absenceTime = request.absenceTime,
            absenceAmount = request.absenceAmount,
            isRemindActive = request.isRemindActive,
            remindOption = request.remindOption,
        ).also {
            studyService.createStudies(it.id)
            userMoitService.join(userId, it.id, UserMoitRole.MASTER)
        }

        return moit.id
    }

    fun getMoitDetails(moitId: Long): MoitDetailsResponse {
        val moit = moitService.getMoitById(moitId)
        val masterId = userMoitService.findMasterUserByMoitId(moit.id).userId

        return MoitDetailsResponse.of(moit, masterId)
    }

    fun joinAsMember(userId: Long, invitationCode: String): MoitJoinResponse {
        val moit = getMoitByInvitationCode(invitationCode)
        return userMoitService.join(userId, moit.id, UserMoitRole.MEMBER).let { MoitJoinResponse.of(it.moitId) }
    }

    fun getMyMoits(userId: Long): MyMoitListResponse {
        val moits = moitService.getMoitsByUserId(userId)
        val ddayByMoitId = moits.associate { moit ->
            moit.id to studyService.findUpcomingStudy(moit.id)
                ?.let { Period.between(LocalDate.now(), it.startAt.toLocalDate()) }?.days
        }

        return moits.sortedWith(compareBy(nullsLast()) { ddayByMoitId[it.id] })
            .map { MyMoitResponseForListView.of(it, ddayByMoitId[it.id]) }
            .let { MyMoitListResponse(it) }
    }

    private fun getMoitByInvitationCode(invitationCode: String): Moit {
        return moitService.getMoitByInvitationCode(invitationCode)
    }
}
