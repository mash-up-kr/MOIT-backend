package com.mashup.moit.domain.user

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.usermoit.UserMoitEntity
import java.time.LocalDateTime

class User(
    val id: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isDeleted: Boolean,
    val providerUniqueKey: String,
    val nickname: String,
    val profileImage: Int,
    val email: String,
    val attendances: List<AttendanceEntity>? = null,
    val userMoits: List<UserMoitEntity>? = null
)
