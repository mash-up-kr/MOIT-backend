package com.mashup.moit.domain.user

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(providerUniqueKey: String, nickname: String, profileImage: Int, email: String): User {
        val userEntity = UserEntity(
            providerUniqueKey = providerUniqueKey,
            nickname = nickname,
            profileImage = profileImage,
            email = email,
            roles = setOf(UserRole.USER)
        )
        return userRepository.save(userEntity)
            .toDomain()
    }

    @Transactional
    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId);
    }

    fun findByProviderUniqueKey(providerUniqueKey: String): User? {
        return userRepository.findByProviderUniqueKey(providerUniqueKey)?.toDomain()
    }

    fun findUserById(userId: Long): User {
        return findUsersById(listOf(userId)).firstOrNull()
            ?: throw MoitException.of(MoitExceptionType.NOT_EXIST)
    }

    fun findByIdOrNull(userId: Long): User? {
        return userRepository.findByIdOrNull(userId)?.toDomain()
    }

    fun findUsersById(userIds: List<Long>): List<User> {
        return userRepository.findAllById(userIds).map { it.toDomain() }
    }
}
