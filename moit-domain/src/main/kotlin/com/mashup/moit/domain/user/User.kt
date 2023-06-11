package com.mashup.moit.domain.user

import com.mashup.moit.domain.attendance.AttendanceEntity
import com.mashup.moit.domain.usermoit.UserMoitEntity
import java.time.LocalDateTime

class User(
    private val id: Long,
    private var createdAt: LocalDateTime,
    private var updatedAt: LocalDateTime,
    private val isDeleted: Boolean,
    private val providerUniqueKey: String,
    private val nickname: String,
    private val profileImage: Int,
    private val email: String,
    private val attendances: List<AttendanceEntity>? = null,
    val userMoits: List<UserMoitEntity>? = null
)
