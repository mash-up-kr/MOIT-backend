package com.mashup.moit.domain.moit

import com.mashup.moit.domain.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "moit")
@Entity
class MoitEntity(
    @Column(name = "name") 
    val name: String,
    
    @Column(name = "description") 
    val description: String? = null,
    
    @Column(name = "profile_url") 
    val profileUrl: String? = null,
    
    @Column(name = "invitation_code") 
    val invitationCode: String,
    
    @Column(name = "is_end") 
    val isEnd: Boolean = false,
    
    @Embedded
    val schedulePolicy: SchedulePolicy,
    
    @Embedded 
    val finePolicy: FinePolicy,
    
    @Embedded 
    val notificationPolicy: NotificationPolicy
) : BaseEntity()
