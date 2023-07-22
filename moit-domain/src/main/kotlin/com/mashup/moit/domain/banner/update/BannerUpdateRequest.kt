package com.mashup.moit.domain.banner.update

sealed interface BannerUpdateRequest

data class StudyAttendanceStartBannerUpdateRequest(
    val studyId: Long,
) : BannerUpdateRequest

data class MoitUnapprovedFineExistBannerUpdateRequest(
    val fineId: Long,
) : BannerUpdateRequest
