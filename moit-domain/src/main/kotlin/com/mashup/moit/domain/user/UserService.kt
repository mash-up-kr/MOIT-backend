package com.mashup.moit.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(providerUniqueKey: String, nickname: String, profileImage: Int, email: String): User {
        return userRepository.save(UserEntity(providerUniqueKey, nickname, profileImage, email)).toDomain()
    }

    fun findByProviderUniqueKey(providerUniqueKey: String): User? {
        return userRepository.findByProviderUniqueKey(providerUniqueKey)?.toDomain()
    }

}
