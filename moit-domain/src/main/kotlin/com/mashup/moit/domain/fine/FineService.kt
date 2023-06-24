package com.mashup.moit.domain.fine

import org.springframework.stereotype.Service

@Service
class FineService(
    private val fineRepository: FineRepository
) {
    fun getFineListByMoitId(moitId: Long): List<Fine> {
        return fineRepository.findByMoitId(moitId)
            .sortedByDescending { it.createdAt }
            .map { it.toDomain() }
    }
}
