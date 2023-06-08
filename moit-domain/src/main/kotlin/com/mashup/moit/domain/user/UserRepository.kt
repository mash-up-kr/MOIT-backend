package com.mashup.moit.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByProviderUniqueKey(providerUniqueKey: String): UserEntity?
    
}
