package com.mashup.moit.domain.fine

import org.springframework.data.jpa.repository.JpaRepository

interface FineRepository : JpaRepository<FineEntity, Long> {
    fun findByMoitId(moitId: Long): List<FineEntity>
    fun findAllByMoitIdAndUserIdAndApproveStatusIn(moitId: Long, userId: Long, approveStatuses: Collection<FineApproveStatus>): List<FineEntity>
}
