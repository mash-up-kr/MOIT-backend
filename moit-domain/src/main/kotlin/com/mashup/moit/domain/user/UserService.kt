package com.mashup.moit.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(user: UserEntity): User {
        return userRepository.save(user).toDomain()
    }

    fun findByProviderUniqueKey(providerUniqueKey: String): User? {
        return userRepository.findByProviderUniqueKey(providerUniqueKey)?.toDomain()
    }

}
