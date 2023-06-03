package com.mashup.moit.domain.usermoit

import com.mashup.moit.domain.common.BaseEntity
import com.mashup.moit.domain.moit.MoitEntity
import com.mashup.moit.domain.user.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "user_moit")
@Entity
class UserMoitEntity(
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moit_id")
    val moit: MoitEntity,
    
    @Column(name = "role")
    val role: String
) : BaseEntity()
