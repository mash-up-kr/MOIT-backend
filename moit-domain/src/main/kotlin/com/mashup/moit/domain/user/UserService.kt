package com.mashup.moit.domain.user

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(providerUniqueKey: String, nickname: String, profileImage: Int, email: String): User {
        return userRepository.save(UserEntity(providerUniqueKey, nickname, profileImage, email, setOf(UserRole.USER))).toDomain()
    }

    fun findByProviderUniqueKey(providerUniqueKey: String): User? {
        return userRepository.findByProviderUniqueKey(providerUniqueKey)?.toDomain()
    }

    fun findUserById(userId: Long): User {
        return findUsersById(listOf(userId)).firstOrNull()
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST)
    }

    fun findUsersById(userIds: List<Long>): List<User> {
        return userRepository.findAllById(userIds).map { it.toDomain() }
    }
}
