package com.mashup.moit.facade

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import com.mashup.moit.controller.moit.dto.MoitCreateRequest
import com.mashup.moit.controller.moit.dto.MoitCreateResponse
import com.mashup.moit.controller.moit.dto.MoitDetailsResponse
import com.mashup.moit.controller.moit.dto.MoitJoinResponse
import com.mashup.moit.controller.moit.dto.MoitJoinUserListResponse
import com.mashup.moit.controller.moit.dto.MoitStudyAttendanceResponse
import com.mashup.moit.controller.moit.dto.MoitStudyListResponse
import com.mashup.moit.controller.moit.dto.MoitStudyResponse
import com.mashup.moit.controller.moit.dto.MyMoitListResponse
import com.mashup.moit.controller.moit.dto.MyMoitResponseForListView
import com.mashup.moit.domain.attendance.AttendanceService
import com.mashup.moit.domain.attendance.AttendanceStatus
import com.mashup.moit.domain.moit.Moit
import com.mashup.moit.domain.moit.MoitService
import com.mashup.moit.domain.study.StudyService
import com.mashup.moit.domain.user.UserService
import com.mashup.moit.domain.usermoit.UserMoitRole
import com.mashup.moit.domain.usermoit.UserMoitService
import com.mashup.moit.infra.aws.s3.S3Service
import com.mashup.moit.infra.event.EventProducer
import com.mashup.moit.infra.event.MoitCreateEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

@Component
class MoitFacade(
    private val moitService: MoitService,
    private val studyService: StudyService,
    private val userMoitService: UserMoitService,
    private val userService: UserService,
    private val attendanceService: AttendanceService,
    private val s3Service: S3Service,
    private val eventProducer: EventProducer,
) {
    @Transactional
    fun create(userId: Long, request: MoitCreateRequest): MoitCreateResponse {
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
            eventProducer.produce(MoitCreateEvent(moitId = it.id))
            userMoitService.join(userId, it.id, UserMoitRole.MASTER)
        }

        return MoitCreateResponse.of(moit)
    }

    fun getMoitDetails(moitId: Long): MoitDetailsResponse {
        val moit = moitService.getMoitById(moitId)
        val masterId = userMoitService.findMasterUserByMoitId(moit.id).userId

        return MoitDetailsResponse.of(moit, masterId)
    }

    fun joinAsMember(userId: Long, invitationCode: String): MoitJoinResponse {
        val moit = getMoitByInvitationCode(invitationCode)
        return userMoitService.join(userId, moit.id, UserMoitRole.MEMBER)
            .let { MoitJoinResponse.of(moit) }
    }

    fun getMoitsByUserId(userId: Long): MyMoitListResponse {
        val moits = moitService.getMoitsByUserId(userId)
        val ddayByMoitId = moits.associate { moit ->
            moit.id to studyService.findUpcomingStudy(moit.id)
                ?.let { Period.between(LocalDate.now(), it.startAt.toLocalDate()) }?.days
        }

        return moits.sortedWith(compareBy(nullsLast()) { ddayByMoitId[it.id] })
            .map { MyMoitResponseForListView.of(it, ddayByMoitId[it.id]) }
            .let { MyMoitListResponse(it) }
    }

    fun getUsersByMoitId(moitId: Long): MoitJoinUserListResponse {
        val userMoits = userMoitService.findUsersByMoitId(moitId)
        val usersByUserId = userService.findUsersById(userMoits.map { it.userId }).associateBy { it.id }

        return MoitJoinUserListResponse.of(
            users = userMoits.map { usersByUserId.getValue(it.userId) },
            masterUserId = userMoits.first { it.role === UserMoitRole.MASTER }.userId,
        )
    }

    fun getAllAttendances(moitId: Long): MoitStudyListResponse {
        val moit = moitService.getMoitById(moitId)
        val studyWithAttendances = studyService.findAllByMoitIdStartAtBefore(moit.id, LocalDateTime.now())
            .mapNotNull { study ->
                val attendances = attendanceService.findAttendancesByStudyId(study.id)
                if (attendances.all { attendance -> attendance.status == AttendanceStatus.UNDECIDED }) {
                    return@mapNotNull null
                }
                study to attendances
            }
        val attendanceUserIds = studyWithAttendances.map { it.second }
            .flatMap { attendances -> attendances.map { attendance -> attendance.userId } }
        val firstAttendanceUsers = userService.findUsersById(attendanceUserIds).toSet()
        return studyWithAttendances.map { (study, attendances) ->
            attendances
                .filterNot { attendance -> attendance.status == AttendanceStatus.UNDECIDED }
                .map { attendance ->
                    MoitStudyAttendanceResponse.of(
                        attendance = attendance,
                        attendanceUser = firstAttendanceUsers.find { user ->
                            user.id == attendance.userId
                        } ?: throw MoitException.of(MoitExceptionType.NOT_EXIST)
                    )
                }
                .let { attendanceResponses -> MoitStudyResponse.of(study, attendanceResponses) }
        }.let(::MoitStudyListResponse)
    }

    fun addMoitImage(moitId: Long, moitImage: MultipartFile) {
        val moit = moitService.getMoitById(moitId)
        val moitImageUrl = s3Service.upload(MOIT_IMAGE_DIRECTORY, moitImage)
        moitService.addMoitImage(moit.id, moitImageUrl)
    }

    private fun getMoitByInvitationCode(invitationCode: String): Moit {
        return moitService.getMoitByInvitationCode(invitationCode)
    }

    companion object {
        private const val MOIT_IMAGE_DIRECTORY = "moit/"
    }

}
